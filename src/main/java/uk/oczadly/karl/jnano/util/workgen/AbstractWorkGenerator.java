/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen;

import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.internal.utils.LimitedCacheMap;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockAccount;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.util.NetworkConstants;
import uk.oczadly.karl.jnano.util.workgen.policy.WorkDifficultyPolicy;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * This abstract class is used for generating work solutions from a given block or root hash.
 *
 * <p>Generation requests will be queued and processed in a FIFO ordering. Requests which utilise the specified work
 * policy object will defer computation and retrieval of the policy until they begin processing, ensuring that
 * time-sensitive policies are still applicable to generated work.</p>
 *
 * <p>Work generations will be globally cached automatically, shared across all implementations. This cache will
 * store up to {@code 100} work generation requests before purging them.</p>
 *
 * <p>Instances of this class should be re-used throughout your application, as each instance will spawn new
 * background threads. This practice also ensures that tasks are queued correctly in the order of request.</p>
 */
public abstract class AbstractWorkGenerator implements WorkGenerator {
    
    private static final ThreadFactory CONSUMER_THREAD_FACTORY =
            JNH.threadFactory("AbstractWorkGenerator-Consumer", true);
    
    private static final Map<HexData, CachedWork> workCache =
            Collections.synchronizedMap(new LimitedCacheMap<>(100));
    
    /** The default Nano difficulty policy. */
    protected static final WorkDifficultyPolicy DEFAULT_POLICY = NetworkConstants.NANO.getWorkDifficulties();
    
    private final ExecutorService requestExecutor = Executors.newSingleThreadExecutor(CONSUMER_THREAD_FACTORY);
    private final WorkDifficultyPolicy policy;
    
    private final Object shutdownLock = new Object();
    private volatile boolean isShutdown, isGenerating;
    
    /**
     * @param policy the work difficulty policy
     */
    protected AbstractWorkGenerator(WorkDifficultyPolicy policy) {
        if (policy == null)
            throw new IllegalArgumentException("Policy cannot be null.");
        this.policy = policy;
    }
    
    
    /**
     * Subclasses must override this method, compute and then return the work solution. The thread will be
     * interrupted if the work was requested to be cancelled.
     * @param root       the work root
     * @param difficulty the work difficulty
     * @param context    additional contextual info on the request
     * @return the computed work solution
     * @throws WorkGenerationException if the work could not be generated
     * @throws InterruptedException    if the thread is interrupted (work cancellation)
     */
    protected abstract WorkSolution generateWork(HexData root, WorkDifficulty difficulty, RequestContext context)
            throws WorkGenerationException, InterruptedException;
    
    
    public final boolean isGenerating() {
        return isGenerating;
    }
    
    /**
     * Returns the work difficulty policy to be used when generating work.
     * @return the work difficulty policy
     */
    public final WorkDifficultyPolicy getDifficultyPolicy() {
        return policy;
    }
    
    
    @Override
    public final FutureWork generate(Block block, WorkDifficulty baseDifficulty) {
        if (block == null)
            throw new IllegalArgumentException("Block cannot be null.");
        if (baseDifficulty == null)
            throw new IllegalArgumentException("Difficulty cannot be null.");
        
        return enqueueWork(new WorkRequestSpec(policy, block, 1, baseDifficulty));
    }
    
    @Override
    public final FutureWork generate(Block block, double diffMultiplier) {
        if (block == null)
            throw new IllegalArgumentException("Block cannot be null.");
        if (diffMultiplier <= 0)
            throw new IllegalArgumentException("Difficulty multiplier must be a positive value.");
    
        return enqueueWork(new WorkRequestSpec(policy, block, diffMultiplier, null));
    }
    
    @Override
    public final FutureWork generate(HexData root, WorkDifficulty baseDifficulty) {
        if (root == null)
            throw new IllegalArgumentException("Root cannot be null.");
        if (baseDifficulty == null)
            throw new IllegalArgumentException("Difficulty cannot be null.");
    
        return enqueueWork(new WorkRequestSpec(policy, root, 1, baseDifficulty));
    }
    
    @Override
    public final FutureWork generate(HexData root, double diffMultiplier) {
        if (root == null)
            throw new IllegalArgumentException("Root cannot be null.");
        if (diffMultiplier <= 0)
            throw new IllegalArgumentException("Difficulty multiplier must be a positive value.");
    
        return enqueueWork(new WorkRequestSpec(policy, root, diffMultiplier, null));
    }
    
    private FutureWork enqueueWork(WorkRequestSpec spec) {
        if (isShutdown())
            throw new IllegalStateException("Work generator is shut down and cannot accept new requests.");
        
        return new FutureWork(requestExecutor.submit(new WorkGeneratorTask(spec)));
    }
    
    
    @Override
    public final boolean isShutdown() {
        return isShutdown;
    }
    
    @Override
    public final void shutdown() {
        if (isShutdown) return;
        synchronized (shutdownLock) {
            if (!isShutdown) {
                isShutdown = true;
                runCleanupThread();
            }
        }
    }
    
    /**
     * Clean up (instances, end connections, terminate threads, etc) when this is called.
     * Work production is guaranteed to have ended when this method is invoked.
     */
    protected void cleanup() {}
    
    private void runCleanupThread() {
        Thread shutdownThread = new Thread(() -> {
            try {
                // Shutdown executor and wait for completion
                requestExecutor.shutdownNow();
                while (!requestExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS));
                // Run cleanup
                cleanup();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
        shutdownThread.setName("WorkGen-CleanupThread-" + shutdownThread.getId());
        shutdownThread.setDaemon(true);
        shutdownThread.start();
    }
    
    @SuppressWarnings("deprecation")
    @Override
    protected final void finalize() {
        shutdown();
    }
    
    
    class WorkGeneratorTask implements Callable<GeneratedWork> {
        private final WorkRequestSpec spec;
        
        public WorkGeneratorTask(WorkRequestSpec spec) {
            this.spec = spec;
        }
        
        @Override
        public GeneratedWork call() throws Exception {
            if (isShutdown)
                throw new WorkGenerationException("Work generator was shut down.");
            
            isGenerating = true;
            try {
                // Fetch request params
                HexData root = spec.root;
                WorkRequestSpec.DifficultySet diff = spec.fetchDifficulty();
                RequestContext context = new RequestContext(spec.block, diff.getMultiplier(), diff.getBase());
                
                // Check cache for precomputed value
                CachedWork cache = workCache.get(root);
                if (cache != null && cache.difficulty.isValid(diff.getTarget()))
                    return new GeneratedWork(cache.work, root, diff.getBase(), diff.getTarget());
                
                // Generate work
                WorkSolution work = generateWork(root, diff.getTarget(), context);
                if (work == null) throw new NullPointerException("Generated work solution was null.");
                
                // Return computed work
                GeneratedWork genWork = new GeneratedWork(work, root, diff.getBase(), diff.getTarget());
                workCache.put(root, new CachedWork(genWork)); // Store in cache
                return genWork;
            } catch (InterruptedException e) {
                if (isShutdown) {
                    throw new WorkGenerationException("Work generator was shut down.", e);
                } else {
                    throw new WorkGenerationException("Work request was cancelled.", e);
                }
            } finally {
                isGenerating = false;
            }
        }
    }
    
    /**
     * Contains additional contextual information on the work request.
     */
    public static class RequestContext {
        private final Block block;
        private final double multiplier;
        private final WorkDifficulty baseDifficulty;
    
        private RequestContext(Block block, double multiplier, WorkDifficulty baseDifficulty) {
            this.block = block;
            this.multiplier = multiplier;
            this.baseDifficulty = baseDifficulty;
        }
    
    
        /**
         * @return the block the work is being generated for
         */
        public Optional<Block> getBlock() {
            return Optional.ofNullable(block);
        }
    
        /**
         * @return the account holder of the block
         * @see #getBlock()
         */
        public Optional<NanoAccount> getAccount() {
            if (block instanceof IBlockAccount)
                return Optional.of(((IBlockAccount)block).getAccount());
            return Optional.empty();
        }
        
        /**
         * @return the difficulty multiplier in respect to the base difficulty
         */
        public double getMultiplier() {
            return multiplier;
        }
    
        /**
         * @return the base threshold difficulty (before multipliers have been applied)
         */
        public WorkDifficulty getBaseDifficulty() {
            return baseDifficulty;
        }
    }
    
    /** Stores the work and difficulty for the given root. */
    private static class CachedWork {
        private final WorkSolution work;
        private final WorkDifficulty difficulty;
        
        public CachedWork(GeneratedWork work) {
            this.work = work.getWork();
            this.difficulty = work.getDifficulty();
        }
    }

}
