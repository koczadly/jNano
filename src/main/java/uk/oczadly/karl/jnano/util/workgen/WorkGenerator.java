/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen;

import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.internal.utils.NanoUtil;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.util.NetworkConstants;
import uk.oczadly.karl.jnano.util.workgen.policy.NodeWorkDifficultyPolicy;
import uk.oczadly.karl.jnano.util.workgen.policy.WorkDifficultyPolicy;

import java.util.concurrent.*;

/**
 * This abstract class is used for generating work solutions from a given block or root hash.
 *
 * <p>The following implementations are provided by this library: {@link CPUWorkGenerator}, {@link NodeWorkGenerator},
 * and {@link DPOWWorkGenerator}.</p>
 *
 * <p>Generation requests will be queued and processed in a FIFO ordering. Requests which utilise the specified work
 * policy object will defer computation and retrieval of the policy until they begin processing, ensuring that
 * time-sensitive policies are still applicable to generated work.</p>
 *
 * <p>Instances of this class should be re-used throughout your application, as each instance will spawn new
 * background threads. This practice also ensures that tasks are queued correctly in the order of request.</p>
 */
public abstract class WorkGenerator {
    
    private static final ThreadFactory CONSUMER_THREAD_FACTORY = JNH.threadFactory("WorkGenerator-Consumer", true);
    
    protected static final WorkDifficultyPolicy DEFAULT_POLICY = NetworkConstants.NANO.getWorkDifficulties();
    
    private final ExecutorService executor = Executors.newSingleThreadExecutor(CONSUMER_THREAD_FACTORY);
    private final WorkDifficultyPolicy policy;
    
    /**
     * @param policy the work difficulty policy (may be null)
     */
    protected WorkGenerator(WorkDifficultyPolicy policy) {
        this.policy = policy;
    }
    
    
    /**
     * Subclasses must override this method, compute and then return the work solution.
     * @param root       the work root
     * @param difficulty the work difficulty
     * @return the computed work solution
     * @throws Exception whenever
     */
    protected abstract WorkSolution generateWork(HexData root, WorkDifficulty difficulty) throws Exception;
    
    
    /**
     * Generates a {@link WorkSolution} for the provided block, using the difficulty retrieved from the difficulty
     * policy.
     *
     * @param block the block to compute work for
     * @return the (future) computed work solution
     * @throws UnsupportedOperationException if no difficulty policy is specified
     *
     * @see WorkDifficultyPolicy#forBlock(Block)
     */
    public final Future<GeneratedWork> generate(Block block) {
        return generate(block, 1);
    }
    
    /**
     * Generates a {@link WorkSolution} for the provided block, using the specified difficulty.
     *
     * @param block      the block to compute work for
     * @param difficulty the minimum difficulty threshold of the work
     * @return the (future) computed work solution
     */
    public final Future<GeneratedWork> generate(Block block, WorkDifficulty difficulty) {
        return generate(NanoUtil.getWorkRoot(block), difficulty);
    }
    
    /**
     * Generates a {@link WorkSolution} for the provided block, using the specified multiplier on top of the
     * specified difficulty policy.
     *
     * <p>The provided difficulty multiplier is stacked on top of the recommended multiplier value returned by the
     * {@link NodeWorkDifficultyPolicy}. For example, if the policy specifies a multiplier of {@code 3} and you
     * provide a multiplier of {@code 2}, the resulting work must be at least {@code 6} times the base difficulty.</p>
     *
     * @param block      the block to compute work for
     * @param multiplier the difficulty multiplier
     * @return the (future) computed work solution
     * @throws UnsupportedOperationException if no difficulty policy is specified
     *
     * @see WorkDifficultyPolicy#forBlock(Block)
     */
    public final Future<GeneratedWork> generate(Block block, double multiplier) {
        if (policy == null)
            throw new UnsupportedOperationException("No difficulty policy is specified.");
        if (block == null)
            throw new IllegalArgumentException("Block cannot be null.");
        if (multiplier <= 0)
            throw new IllegalArgumentException("Difficulty multiplier must be a positive value.");
        return enqueueWork(new WorkRequestSpec.WithBlock(policy, block, multiplier));
    }
    
    /**
     * Generates a {@link WorkSolution} for the provided block root hash using the "any" difficulty provided by the
     * difficulty policy.
     *
     * @param root the root hash
     * @return the (future) computed work solution
     * @throws UnsupportedOperationException if no difficulty policy is specified
     *
     * @see WorkSolution#getRoot(Block)
     * @see WorkDifficultyPolicy#forAny()
     */
    public final Future<GeneratedWork> generate(HexData root) {
        if (policy == null)
            throw new UnsupportedOperationException("No difficulty policy is specified.");
        if (root == null)
            throw new IllegalArgumentException("Root cannot be null.");
        return enqueueWork(new WorkRequestSpec.WithRoot(policy, root, null));
    }
    
    /**
     * Generates a {@link WorkSolution} for the provided block root hash, using the specified difficulty.
     *
     * @param root       the root hash
     * @param difficulty the minimum difficulty threshold of the work
     * @return the (future) computed work solution
     *
     * @see WorkSolution#getRoot(Block)
     */
    public final Future<GeneratedWork> generate(HexData root, WorkDifficulty difficulty) {
        if (root == null)
            throw new IllegalArgumentException("Root cannot be null.");
        if (difficulty == null)
            throw new IllegalArgumentException("Difficulty cannot be null.");
        return enqueueWork(new WorkRequestSpec.WithRoot(policy, root, difficulty));
    }
    
    
    /**
     * Returns whether this generator has been shut down by calling {@link #shutdown()}.
     * @return true if this generator has been shut down.
     */
    public final boolean isShutdown() {
        return executor.isShutdown();
    }
    
    /**
     * Attempts to cancel all pending work generations, and stops the main consumer thread from running.
     */
    public void shutdown() {
        executor.shutdownNow();
    }
    
    @SuppressWarnings("deprecation")
    @Override
    protected final void finalize() throws Throwable {
        try {
            shutdown();
        } finally {
            super.finalize();
        }
    }
    
    private Future<GeneratedWork> enqueueWork(WorkRequestSpec spec) {
        if (executor.isShutdown())
            throw new IllegalStateException("Work generator is shut down and cannot accept new requests.");
        
        return executor.submit(new WorkGeneratorTask(spec));
    }
    
    
    class WorkGeneratorTask implements Callable<GeneratedWork> {
        private final WorkRequestSpec spec;
        
        public WorkGeneratorTask(WorkRequestSpec spec) {
            this.spec = spec;
        }
    
        @Override
        public GeneratedWork call() throws Exception {
            HexData root = spec.getRoot();
            WorkRequestSpec.DifficultySet difficulty = spec.getDifficulty();
            
            WorkSolution work = generateWork(root, difficulty.getTarget());
            return new GeneratedWork(work, root, difficulty.getBase(), difficulty.getTarget());
        }
    }

}
