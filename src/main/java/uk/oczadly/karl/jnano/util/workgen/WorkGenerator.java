/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen;

import uk.oczadly.karl.jnano.internal.utils.NanoUtil;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.util.NanoConstants;
import uk.oczadly.karl.jnano.util.workgen.policy.NodeWorkDifficultyPolicy;
import uk.oczadly.karl.jnano.util.workgen.policy.WorkDifficultyPolicy;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This abstract class is used for generating work solutions from a given block or root hash.
 *
 * <p>The following implementations are provided by this library: {@link CPUWorkGenerator},
 * {@link NodeWorkGenerator}.</p>
 *
 * <p>Generation requests will be queued and processed in a FIFO ordering. Requests which utilise the specified work
 * policy object will defer computation and retrieval of the policy until they begin processing, ensuring that
 * time-sensitive policies are still applicable to generated work.</p>
 *
 * <p>Instances of this class should be re-used throughout your application, as each instance will spawn a new
 * background thread. This also ensures that tasks are queued correctly in the order of request.</p>
 */
//TODO: allow cancellation of work
public abstract class WorkGenerator {
    
    static final WorkDifficultyPolicy DEFAULT_POLICY = NanoConstants.NANO_LIVE_NET.getWorkDifficulties();
    
    private final WorkDifficultyPolicy policy;
    
    private final BlockingQueue<WorkRequest> queue = new LinkedBlockingQueue<>();
    private final Thread consumerThread = new ConsumerThread();
    
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
     * <p>Note that cancelling work requests is currently not supported, and calls to the {@code cancel} method of the
     * returned future object will be ignored.</p>
     *
     * @param block the block to compute work for
     * @return the (future) computed work solution
     * @throws UnsupportedOperationException if no difficulty policy is specified
     *
     * @see WorkDifficultyPolicy#forBlock(Block)
     */
    public final Future<WorkSolution> generate(Block block) {
        return generate(block, 1);
    }
    
    /**
     * Generates a {@link WorkSolution} for the provided block, using the specified difficulty.
     *
     * <p>Note that cancelling work requests is currently not supported, and calls to the {@code cancel} method of the
     * returned future object will be ignored.</p>
     *
     * @param block      the block to compute work for
     * @param difficulty the minimum difficulty threshold of the work
     * @return the (future) computed work solution
     */
    public final Future<WorkSolution> generate(Block block, WorkDifficulty difficulty) {
        return generate(NanoUtil.getWorkRoot(block), difficulty);
    }
    
    /**
     * Generates a {@link WorkSolution} for the provided block, using the specified multiplier on top of the
     * specified difficulty policy.
     *
     * <p>The provided difficulty multiplier is applied to the absolute difficulty retrieved from the difficulty
     * policy. If the difficulty policy applies it's own multiplier (as is typically the case with
     * {@link NodeWorkDifficultyPolicy}), then this multiplier will be stacked on top of the previous
     * multiplication, rather than overriding.</p>
     *
     * <p>Note that cancelling work requests is currently not supported, and calls to the {@code cancel} method of the
     * returned future object will be ignored.</p>
     *
     * @param block      the block to compute work for
     * @param multiplier the difficulty multiplier
     * @return the (future) computed work solution
     * @throws UnsupportedOperationException if no difficulty policy is specified
     *
     * @see WorkDifficultyPolicy#forBlock(Block)
     */
    public final Future<WorkSolution> generate(Block block, double multiplier) {
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
     * <p>Note that cancelling work requests is currently not supported, and calls to the {@code cancel} method of the
     * returned future object will be ignored.</p>
     *
     * @param root the root hash
     * @return the (future) computed work solution
     * @throws UnsupportedOperationException if no difficulty policy is specified
     *
     * @see WorkSolution#getRoot(Block)
     * @see WorkDifficultyPolicy#forAny()
     */
    public final Future<WorkSolution> generate(HexData root) {
        if (policy == null)
            throw new UnsupportedOperationException("No difficulty policy is specified.");
        if (root == null)
            throw new IllegalArgumentException("Root cannot be null.");
        return enqueueWork(new WorkRequestSpec.WithRoot(policy, root, null));
    }
    
    /**
     * Generates a {@link WorkSolution} for the provided block root hash, using the specified difficulty.
     *
     * <p>Note that cancelling work requests is currently not supported, and calls to the {@code cancel} method of the
     * returned future object will be ignored.</p>
     *
     * @param root       the root hash
     * @param difficulty the minimum difficulty threshold of the work
     * @return the (future) computed work solution
     *
     * @see WorkSolution#getRoot(Block)
     */
    public final Future<WorkSolution> generate(HexData root, WorkDifficulty difficulty) {
        if (root == null)
            throw new IllegalArgumentException("Root cannot be null.");
        if (difficulty == null)
            throw new IllegalArgumentException("Difficulty cannot be null.");
        return enqueueWork(new WorkRequestSpec.WithRoot(policy, root, difficulty));
    }
    
    
    /**
     * Attempts to cancel all pending work generations, and stops the main consumer thread from running.
     */
    public final void shutdown() {
        consumerThread.interrupt();
        queue.clear();
    }
    
    
    private Future<WorkSolution> enqueueWork(WorkRequestSpec spec) {
        WorkRequest workReq = new WorkRequest(spec);
        queue.add(workReq);
        
        // Start consumer thread (if not running)
        if (!consumerThread.isAlive()) {
            synchronized (this) {
                if (!consumerThread.isAlive())
                    consumerThread.start();
            }
        }
        
        return workReq.future;
    }
    
    
    static class WorkRequest {
        final WorkRequestSpec spec;
        final CompletableFuture<WorkSolution> future = new FutureWork();
        
        WorkRequest(WorkRequestSpec requestSpec) {
            this.spec = requestSpec;
        }
    }
    
    private static final AtomicInteger THREAD_ID = new AtomicInteger();
    class ConsumerThread extends Thread {
        public ConsumerThread() {
            super("WorkGenerator-Consumer-" + THREAD_ID.getAndIncrement());
            setDaemon(true);
        }
    
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                // Retrieve request
                WorkRequest request;
                try {
                    request = queue.take();
                } catch (InterruptedException e) {
                    break;
                }
                
                try {
                    WorkSolution work = generateWork(request.spec.getRoot(), request.spec.getDifficulty());
                    request.future.complete(work);
                } catch (Throwable t) {
                    request.future.completeExceptionally(t);
                }
            }
        }
    }

}
