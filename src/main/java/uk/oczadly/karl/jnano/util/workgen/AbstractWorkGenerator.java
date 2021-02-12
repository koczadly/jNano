/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen;

import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockAccount;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.util.NetworkConstants;
import uk.oczadly.karl.jnano.util.workgen.policy.WorkDifficultyPolicy;

import java.util.Optional;
import java.util.concurrent.*;

/**
 * This abstract class is used for generating work solutions from a given block or root hash.
 *
 * <p>Generation requests will be queued and processed in a FIFO ordering. Requests which utilise the specified work
 * policy object will defer computation and retrieval of the policy until they begin processing, ensuring that
 * time-sensitive policies are still applicable to generated work.</p>
 *
 * <p>Instances of this class should be re-used throughout your application, as each instance will spawn new
 * background threads. This practice also ensures that tasks are queued correctly in the order of request.</p>
 */
public abstract class AbstractWorkGenerator implements WorkGenerator {
    
    private static final ThreadFactory CONSUMER_THREAD_FACTORY = JNH.threadFactory("WorkGenerator-Consumer", true);
    
    /** The default Nano difficulty policy. */
    protected static final WorkDifficultyPolicy DEFAULT_POLICY = NetworkConstants.NANO.getWorkDifficulties();
    
    private final ExecutorService executor = Executors.newSingleThreadExecutor(CONSUMER_THREAD_FACTORY);
    private final WorkDifficultyPolicy policy;
    
    /**
     * @param policy the work difficulty policy
     */
    protected AbstractWorkGenerator(WorkDifficultyPolicy policy) {
        if (policy == null)
            throw new IllegalArgumentException("Policy cannot be null.");
        this.policy = policy;
    }
    
    
    /**
     * Subclasses must override this method, compute and then return the work solution.
     * @param root       the work root
     * @param difficulty the work difficulty
     * @param context    additional contextual info on the request
     * @return the computed work solution
     * @throws Exception whenever
     */
    protected abstract WorkSolution generateWork(HexData root, WorkDifficulty difficulty, RequestContext context)
            throws Exception;
    
    
    @Override
    public Future<GeneratedWork> generate(Block block, WorkDifficulty baseDifficulty) {
        if (block == null)
            throw new IllegalArgumentException("Block cannot be null.");
        if (baseDifficulty == null)
            throw new IllegalArgumentException("Difficulty cannot be null.");
        
        return enqueueWork(new WorkRequestSpec(policy, block, 1, baseDifficulty));
    }
    
    @Override
    public final Future<GeneratedWork> generate(Block block, double diffMultiplier) {
        if (block == null)
            throw new IllegalArgumentException("Block cannot be null.");
        if (diffMultiplier <= 0)
            throw new IllegalArgumentException("Difficulty multiplier must be a positive value.");
    
        return enqueueWork(new WorkRequestSpec(policy, block, diffMultiplier, null));
    }
    
    @Override
    public final Future<GeneratedWork> generate(HexData root, WorkDifficulty baseDifficulty) {
        if (root == null)
            throw new IllegalArgumentException("Root cannot be null.");
        if (baseDifficulty == null)
            throw new IllegalArgumentException("Difficulty cannot be null.");
    
        return enqueueWork(new WorkRequestSpec(policy, root, 1, baseDifficulty));
    }
    
    @Override
    public Future<GeneratedWork> generate(HexData root, double diffMultiplier) {
        if (root == null)
            throw new IllegalArgumentException("Root cannot be null.");
        if (diffMultiplier <= 0)
            throw new IllegalArgumentException("Difficulty multiplier must be a positive value.");
    
        return enqueueWork(new WorkRequestSpec(policy, root, diffMultiplier, null));
    }
    
    
    @Override
    public final WorkDifficultyPolicy getDifficultyPolicy() {
        return policy;
    }
    
    @Override
    public final boolean isShutdown() {
        return executor.isShutdown();
    }
    
    @Override
    public void shutdown() {
        executor.shutdownNow();
    }
    
    private Future<GeneratedWork> enqueueWork(WorkRequestSpec spec) {
        if (executor.isShutdown())
            throw new IllegalStateException("Work generator is shut down and cannot accept new requests.");
        
        return executor.submit(new WorkGeneratorTask(spec));
    }
    
    
    @SuppressWarnings("deprecation")
    @Override
    protected void finalize() throws Throwable {
        try {
            shutdown();
        } finally {
            super.finalize();
        }
    }
    
    
    class WorkGeneratorTask implements Callable<GeneratedWork> {
        private final WorkRequestSpec spec;
        
        public WorkGeneratorTask(WorkRequestSpec spec) {
            this.spec = spec;
        }
    
        @Override
        public GeneratedWork call() throws Exception {
            HexData root = spec.root;
            WorkRequestSpec.DifficultySet difficulty = spec.fetchDifficulty();
            RequestContext context = new RequestContext(spec.block, difficulty.getMultiplier(), difficulty.getBase());
            
            WorkSolution work = generateWork(root, difficulty.getTarget(), context);
            return new GeneratedWork(work, root, difficulty.getBase(), difficulty.getTarget());
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
            if (block != null && block instanceof IBlockAccount) {
                return Optional.ofNullable(((IBlockAccount)block).getAccount());
            }
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

}
