/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen;

import com.rfksystems.blake2b.Blake2b;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.util.workgen.policy.WorkDifficultyPolicy;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * This {@code WorkGenerator} computes the work solution on the system's CPU within the JVM, without requiring an
 * external tool or node.
 *
 * <p>Use of this generation method is not recommended unless necessary, as the calculations are likely to be less
 * efficient than other methods of computation available.</p>
 *
 * <p>Instances of this class should be re-used throughout your application, as each instance will spawn new
 * background threads. This practice also ensures that tasks are queued correctly in the order of request.</p>
 */
public class CPUWorkGenerator extends WorkGenerator {
    
    private static final ThreadFactory WORKER_THREAD_FACTORY = JNH.threadFactory("CPUWorkGenerator-Worker", true);
    private static final Random RANDOM = new Random();
    
    private final int threadCount;
    private final ExecutorService executorService;
    
    /**
     * Constructs a {@code CPUWorkGenerator} with the default Nano difficulty policy, and {@code n-1} threads, where
     * {@code n} is the CPU core count.
     */
    public CPUWorkGenerator() {
        this(WorkGenerator.DEFAULT_POLICY);
    }
    
    /**
     * Constructs a {@code CPUWorkGenerator} with the default Nano difficulty policy, using the specified thread count.
     *
     * @param threadCount the number of threads to compute with
     */
    public CPUWorkGenerator(int threadCount) {
        this(WorkGenerator.DEFAULT_POLICY, threadCount);
    }
    
    /**
     * Constructs a {@code CPUWorkGenerator} using the provided difficulty policy, and {@code n-1} threads, where
     * {@code n} is the CPU core count.
     *
     * @param policy the difficulty policy to use (may be null)
     */
    public CPUWorkGenerator(WorkDifficultyPolicy policy) {
        this(policy, Math.max(Runtime.getRuntime().availableProcessors() - 1, 1));
    }
    
    /**
     * Constructs a {@code CPUWorkGenerator} using the provided difficulty policy and thread count.
     *
     * @param policy      the difficulty policy to use (may be null)
     * @param threadCount the number of threads to compute with
     */
    public CPUWorkGenerator(WorkDifficultyPolicy policy, int threadCount) {
        super(policy);
        if (threadCount < 1)
            throw new IllegalArgumentException("Must have at least 1 thread.");
        this.threadCount = threadCount;
        this.executorService = Executors.newFixedThreadPool(threadCount, WORKER_THREAD_FACTORY);
    }
    
    
    /**
     * Returns the number of threads specified to compute work solutions.
     * @return the number of threads used
     */
    public int getThreadCount() {
        return threadCount;
    }
    
    @Override
    public void shutdown() {
        try {
            executorService.shutdownNow();
        } finally {
            super.shutdown();
        }
    }
    
    @Override
    protected WorkSolution generateWork(HexData root, WorkDifficulty difficulty) throws Exception {
        // Prepare parameters
        byte[] rootBytes = root.toByteArray();
        byte[] thresholdBytes = JNH.reverseArray(JNH.longToBytes(difficulty.getAsLong()));
        int[] threshold = new int[8];
        for (int i = 0; i < 8; i++)
            threshold[i] = thresholdBytes[i] & 0xFF;
        byte[] initialWorkTemplate = new byte[8];
        RANDOM.nextBytes(initialWorkTemplate);
    
        CompletableFuture<WorkSolution> result = new CompletableFuture<>();
        
        // Submit tasks to executor
        for (int i = 0; i < threadCount; i++) {
            byte[] initialWork = Arrays.copyOf(initialWorkTemplate, initialWorkTemplate.length);
            initialWork[7] += (byte)(i * 37); // Try to ensure different work value for each thread
    
            executorService.submit(new GeneratorTask(rootBytes, threshold, initialWork, result));
        }
    
        return result.get();
    }
    
    static class GeneratorTask implements Runnable {
        final byte[] root, initalWork;
        final int[] threshold;
        final CompletableFuture<WorkSolution> result;
    
        public GeneratorTask(byte[] root, int[] threshold, byte[] initalWork, CompletableFuture<WorkSolution> result) {
            this.root = root;
            this.threshold = threshold;
            this.initalWork = initalWork;
            this.result = result;
        }
    
        @Override
        public void run() {
            Blake2b digest = new Blake2b(null, 8, null, null);
            byte[] difficulty = new byte[8];
    
            byte[] bytes = new byte[40]; // work[8], root[32]
            System.arraycopy(initalWork, 0, bytes, 0, 8);
            System.arraycopy(root,       0, bytes, 8, 32);
    
            Thread thisThread = Thread.currentThread();
            
            while (!thisThread.isInterrupted() && !result.isDone()) {
                // Compute a batch of 256 iterations
                byte i = 0;
                do {
                    bytes[0] = i;
                    
                    // Hash digest
                    digest.update(bytes, 0, 40);
                    digest.digest(difficulty, 0);
                    
                    // Validate against threshold
                    boolean isValid = true;
                    for (int j = 0; j < 8; j++) {
                        if ((difficulty[j] & 0xFF) < threshold[j]) {
                            isValid = false;
                            break;
                        }
                    }
                    
                    // Check and handle valid responses
                    if (isValid) {
                        byte[] workBytes = JNH.reverseArray(Arrays.copyOfRange(bytes, 0, 8));
                        WorkSolution work = new WorkSolution(JNH.bytesToLong(workBytes));
                        result.complete(work);
                        return;
                    }
                } while (++i != 0);
                
                // Increment work value
                for (int j = 1; j < 8; j++) {
                    if (++bytes[j] != 0) break;
                }
            }
            result.completeExceptionally(new InterruptedException("Work task interrupted."));
        }
    }
    
}
