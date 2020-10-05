/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.work;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.rfksystems.blake2b.Blake2b;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockAccount;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockPrevious;
import uk.oczadly.karl.jnano.rpc.request.node.RequestWorkGenerate;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class represents a proof-of-work solution.
 */
@JsonAdapter(WorkSolution.WorkSolutionJsonAdapter.class)
public class WorkSolution {
    
    private static final Random RANDOM = new Random();
    private static final ExecutorService WORK_GEN_POOL = Executors.newWorkStealingPool();
    
    private final long longVal;
    private final String hexVal;
    
    /**
     * @param hexVal the work solution, encoded as a hexadecimal string
     */
    public WorkSolution(String hexVal) {
        this(Long.parseUnsignedLong(hexVal.startsWith("0x") ? hexVal.substring(2) : hexVal, 16));
    }
    
    /**
     * @param longVal the work solution, encoded as an unsigned long
     */
    public WorkSolution(long longVal) {
        this.longVal = longVal;
        this.hexVal = JNH.leftPadString(Long.toHexString(longVal), 16, '0');
    }
    
    
    /**
     * @return the absolute difficulty value, encoded as an unsigned long
     */
    public long getAsLong() {
        return longVal;
    }
    
    /**
     * @return the absolute difficulty value in hexadecimal format
     */
    public String getAsHexadecimal() {
        return hexVal;
    }
    
    
    /**
     * Calculates the difficulty for a given block. The block type must implement either the {@link IBlockAccount} or
     * {@link IBlockPrevious} interface, otherwise an {@link IllegalArgumentException} will be thrown.
     * @param block the block to calculate the difficulty for
     * @return the difficulty of this work solution for the given root hash
     * @throws IllegalArgumentException if the block does not contain a {@code previous} or {@code account} field
     */
    public WorkDifficulty calculateDifficulty(Block block) {
        return calculateDifficulty(getRoot(block));
    }
    
    /**
     * Calculates the difficulty for a given root hash. The root value should be either the previous block hash for
     * existing accounts, or the account's public key for the first block.
     * @param root the root hash (64 character hex string)
     * @return the difficulty of this work solution for the given root hash
     * @see #getRoot(Block)
     * @see #calculateDifficulty(Block)
     */
    public WorkDifficulty calculateDifficulty(String root) {
        if (root == null) throw new IllegalArgumentException("Root cannot be null.");
        if (!JNH.isValidHex(root, 64))
            throw new IllegalArgumentException("Root argument must be a 64-character hex string.");
        
        return calculateDifficulty(JNH.ENC_16.decode(root));
    }
    
    /**
     * Calculates the difficulty for a given root hash. The root value should be either the previous block hash for
     * existing accounts, or the account's public key for the first block.
     * @param root the root hash (32 element byte array)
     * @return the difficulty of this work solution for the given root hash
     */
    public WorkDifficulty calculateDifficulty(byte[] root) {
        if (root == null) throw new IllegalArgumentException("Root array cannot be null.");
        if (root.length != 32) throw new IllegalArgumentException("Root array must have a length of 32.");
    
        return new WorkDifficulty(convertBytesToLong(JNH.blake2b(8, convertLongToBytes(longVal), root)));
    }
    
    
    @Override
    public String toString() {
        return getAsHexadecimal();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkSolution)) return false;
        WorkSolution that = (WorkSolution)o;
        return longVal == that.longVal;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(longVal);
    }
    
    
    /**
     * Returns the root data for the given block, for use in work calculations. The block type must implement either the
     * {@link IBlockAccount} or {@link IBlockPrevious} interface, otherwise an {@link IllegalArgumentException} will
     * be thrown.
     * @param block the block to calculate the root of
     * @return the root hash of the given block
     * @throws IllegalArgumentException if the block does not contain a {@code previous} or {@code account} field
     */
    public static String getRoot(Block block) {
        if (block == null) throw new IllegalArgumentException("Block cannot be null.");
        
        // Try previous
        if (block instanceof IBlockPrevious) {
            HexData previous = ((IBlockPrevious)block).getPreviousBlockHash();
            if (!JNH.isZero(previous, true))
                return previous.toHexString();
        }
        // Try account
        if (block instanceof IBlockAccount) {
            NanoAccount account = ((IBlockAccount)block).getAccount();
            if (account != null)
                return account.toPublicKey();
        }
        throw new IllegalArgumentException("The root hash cannot be determined from the given block.");
    }
    
    
    /**
     * <p>Generates a work solution from the given root and minimum difficulty threshold. The root value should be
     * either the previous block hash for existing accounts, or the account's public key for the first block.</p>
     * <p><strong>CAUTION:</strong> This method will generate the work on the CPU. For GPU calculations, use the
     * work generation utility provided by the node through RPC ({@link RequestWorkGenerate}).</p>
     * @param root      the root hash (64 character hex string)
     * @param threshold the minimum difficulty threshold
     * @return the generated work solution
     * @throws InterruptedException if the thread is interrupted
     * @see #generateMultiThreaded(String, WorkDifficulty)
     */
    public static WorkSolution generate(String root, WorkDifficulty threshold) throws InterruptedException {
        if (root == null) throw new IllegalArgumentException("Root argument cannot be null.");
        if (!JNH.isValidHex(root, 64))
            throw new IllegalArgumentException("Root argument must be a 64-character hex string.");
        
        return generate(JNH.ENC_16.decode(root), threshold);
    }
    
    /**
     * <p>Generates a work solution from the given root and minimum difficulty threshold. The root value should be
     * either the previous block hash for existing accounts, or the account's public key for the first block.</p>
     * <p><strong>CAUTION:</strong> This method will generate the work on the CPU. For GPU calculations, use the
     * work generation utility provided by the node through RPC ({@link RequestWorkGenerate}).</p>
     * @param root      the root bytes (32 element byte array)
     * @param threshold the minimum difficulty threshold
     * @return the generated work solution
     * @throws InterruptedException if the thread is interrupted
     * @see #generateMultiThreaded(byte[], WorkDifficulty)
     */
    public static WorkSolution generate(byte[] root, WorkDifficulty threshold) throws InterruptedException {
        if (root == null) throw new IllegalArgumentException("Root array cannot be null.");
        if (root.length != 32) throw new IllegalArgumentException("Root array must have a length of 32.");
        if (threshold == null) throw new IllegalArgumentException("Difficulty threshold cannot be null.");
        
        byte[] thresholdBytes = convertLongToBytes(threshold.getAsLong());
        byte[] initialWork = new byte[8];
        RANDOM.nextBytes(initialWork);
        return generate(root, thresholdBytes, initialWork, null);
    }
    
    /**
     * <p>Generates a work solution from the given root and minimum difficulty threshold. The root value should be
     * either the previous block hash for existing accounts, or the account's public key for the first block. This
     * variant of the generate method will utilise all of the systems CPU cores.</p>
     * <p><strong>CAUTION:</strong> This method will generate the work on the CPU. For GPU calculations, use the
     * work generation utility provided by the node through RPC ({@link RequestWorkGenerate}).</p>
     * @param root      the root hash (64 character hex string)
     * @param threshold the minimum difficulty threshold
     * @return a future object, representing the generated work solution
     */
    public static Future<WorkSolution> generateMultiThreaded(String root, WorkDifficulty threshold) {
        return generateMultiThreaded(root, threshold, WORK_GEN_POOL, Runtime.getRuntime().availableProcessors());
    }
    
    /**
     * <p>Generates a work solution from the given root and minimum difficulty threshold. The root value should be
     * either the previous block hash for existing accounts, or the account's public key for the first block. This
     * variant of the generate method will utilise all of the systems CPU cores.</p>
     * <p><strong>CAUTION:</strong> This method will generate the work on the CPU. For GPU calculations, use the
     * work generation utility provided by the node through RPC ({@link RequestWorkGenerate}).</p>
     * @param root      the root bytes (32 element byte array)
     * @param threshold the minimum difficulty threshold
     * @return a future object, representing the generated work solution
     */
    public static Future<WorkSolution> generateMultiThreaded(byte[] root, WorkDifficulty threshold) {
        return generateMultiThreaded(root, threshold, WORK_GEN_POOL, Runtime.getRuntime().availableProcessors());
    }
    
    /**
     * <p>Generates a work solution from the given root and minimum difficulty threshold. The root value should be
     * either the previous block hash for existing accounts, or the account's public key for the first block.</p>
     * <p>This variant of the generate method will submit the number of {@code parallelTasks} specified to the given
     * {@code executor}. Once a valid work solution has been found by any of the created tasks, they will all
     * automatically end and discard themselves.</p>
     * <p><strong>CAUTION:</strong> This method will generate the work on the CPU. For GPU calculations, use the
     * work generation utility provided by the node through RPC ({@link RequestWorkGenerate}).</p>
     * @param root          the root hash (64 character hex string)
     * @param threshold     the minimum difficulty threshold
     * @param executor      the {@link ExecutorService} to submit the work generation tasks to
     * @param parallelTasks the number of tasks to submit to the executor service
     * @return a future object, representing the generated work solution
     */
    public static Future<WorkSolution> generateMultiThreaded(String root, WorkDifficulty threshold,
                                                             ExecutorService executor, int parallelTasks) {
        if (root == null) throw new IllegalArgumentException("Root argument cannot be null.");
        if (!JNH.isValidHex(root, 64))
            throw new IllegalArgumentException("Root argument must be a 64-character hex string.");
    
        return generateMultiThreaded(JNH.ENC_16.decode(root), threshold, executor, parallelTasks);
    }
    
    /**
     * <p>Generates a work solution from the given root and minimum difficulty threshold. The root value should be
     * either the previous block hash for existing accounts, or the account's public key for the first block.</p>
     * <p>This variant of the generate method will submit the number of {@code parallelTasks} specified to the given
     * {@code executor}. Once a valid work solution has been found by any of the created tasks, they will all
     * automatically end and discard themselves.</p>
     * <p><strong>CAUTION:</strong> This method will generate the work on the CPU. For GPU calculations, use the
     * work generation utility provided by the node through RPC ({@link RequestWorkGenerate}).</p>
     * @param root          the root bytes (32 element byte array)
     * @param threshold     the minimum difficulty threshold
     * @param executor      the {@link ExecutorService} to submit the work generation tasks to
     * @param parallelTasks the number of tasks to submit to the executor service
     * @return a future object, representing the generated work solution
     */
    public static Future<WorkSolution> generateMultiThreaded(byte[] root, WorkDifficulty threshold,
                                                             ExecutorService executor, int parallelTasks) {
        if (root == null) throw new IllegalArgumentException("Root array cannot be null.");
        if (root.length != 32) throw new IllegalArgumentException("Root array must have a length of 32.");
        if (threshold == null) throw new IllegalArgumentException("Difficulty threshold cannot be null.");
        if (parallelTasks < 1) throw new IllegalArgumentException("Parallel tasks must be 1 or greater.");
        
        final CompletableFuture<WorkSolution> future = new CompletableFuture<>();
        byte[] thresholdBytes = convertLongToBytes(threshold.getAsLong());
        byte[] initialWork = new byte[8];
        RANDOM.nextBytes(initialWork); // Populate initial work array with random bytes
        AtomicBoolean interrupt = new AtomicBoolean(false);
        
        for (int i=0; i<parallelTasks; i++) {
            byte[] work = Arrays.copyOf(initialWork, initialWork.length);
            work[7] += i * 2; // Ensure last (MSB) byte is different for each thread
    
            executor.execute(() -> {
                try {
                    WorkSolution result = generate(root, thresholdBytes, work, interrupt);
                    future.complete(result);
                    interrupt.set(true);
                } catch (InterruptedException e) {
                    // In case thread is interrupted from an external cause
                    future.completeExceptionally(e);
                    interrupt.set(true);
                }
            });
        }
        return future;
    }
    
    
    private static WorkSolution generate(byte[] root, byte[] thresholdBytes, byte[] work, AtomicBoolean interrupt)
            throws InterruptedException {
        Blake2b digest = new Blake2b(null, 8, null, null);
        byte[] difficulty = new byte[8];
        
        Thread thread = Thread.currentThread();
        
        while (true) {
            if ((interrupt != null && interrupt.get()) || thread.isInterrupted())
                throw new InterruptedException();
            
            // Hash digest
            digest.update(work, 0, work.length);
            digest.update(root, 0, root.length);
            digest.digest(difficulty, 0);
            
            // Compare against threshold
            boolean valid = true;
            for (int i=0; i<thresholdBytes.length; i++) {
                if (Byte.compareUnsigned(difficulty[i], thresholdBytes[i]) < 0) {
                    valid = false;
                    break;
                }
            }
            if (valid)
                return new WorkSolution(convertBytesToLong(work));
            
            // Increment 'work' array
            for (int i=0; i<work.length; i++) {
                if (++work[i] != 0) break;
            }
        }
    }
    
    private static byte[] convertLongToBytes(long val) {
        return JNH.reverseArray(JNH.longToBytes(val));
    }
    
    private static long convertBytesToLong(byte[] val) {
        return JNH.bytesToLong(JNH.reverseArray(val));
    }
    
    
    
    static class WorkSolutionJsonAdapter implements JsonSerializer<WorkSolution>,
            JsonDeserializer<WorkSolution> {
        @Override
        public WorkSolution deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return new WorkSolution(json.getAsString());
        }
        
        @Override
        public JsonElement serialize(WorkSolution src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getAsHexadecimal());
        }
    }
    
}
