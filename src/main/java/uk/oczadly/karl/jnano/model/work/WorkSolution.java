package uk.oczadly.karl.jnano.model.work;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.rfksystems.blake2b.Blake2b;
import uk.oczadly.karl.jnano.internal.JNanoHelper;
import uk.oczadly.karl.jnano.rpc.request.node.RequestWorkGenerate;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Random;

/**
 * This class represents a proof-of-work solution.
 */
@JsonAdapter(WorkSolution.WorkSolutionJsonAdapter.class)
public class WorkSolution {

    private final long longVal;
    private final String hexVal;
    
    public WorkSolution(String hexVal) {
        this(Long.parseUnsignedLong(hexVal.startsWith("0x") ? hexVal.substring(2) : hexVal, 16));
    }
    
    public WorkSolution(long longVal) {
        this.longVal = longVal;
    
        // Convert hex to 16 char length
        String hex = Long.toHexString(longVal);
        StringBuilder sb = new StringBuilder(16);
        for (int i=0; i<(16-hex.length()); i++)
            sb.append('0');
        sb.append(hex);
        this.hexVal = sb.toString();
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
     * Calculates the difficulty for a given root hash. The root value should be either the block hash for existing
     * accounts, or the account's public key for the first block.
     * @param root the root hash (64 character hex string)
     * @return the difficulty of this work solution for the given root hash
     */
    public WorkDifficulty calculateDifficulty(String root) {
        if (root == null) throw new IllegalArgumentException("Root argument cannot be null.");
        if (!JNanoHelper.isValidHex(root, 64))
            throw new IllegalArgumentException("Root argument must be a 64-character hex string.");
        
        return calculateDifficulty(JNanoHelper.ENCODER_HEX.decode(root));
    }
    
    /**
     * Calculates the difficulty for a given root hash. The root value should be either the block hash for existing
     * accounts, or the account's public key for the first block.
     * @param root the root hash (32 element byte array)
     * @return the difficulty of this work solution for the given root hash
     */
    public WorkDifficulty calculateDifficulty(byte[] root) {
        if (root == null) throw new IllegalArgumentException("Root array cannot be null.");
        if (root.length != 32) throw new IllegalArgumentException("Root array must have a length of 32.");
    
        return new WorkDifficulty(convertBytesToLong(JNanoHelper.blake2b(8, convertLongToBytes(longVal), root)));
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
     * <p>Generates a work solution from the given root and minimum difficulty threshold. The root value should be
     * either the block hash for existing accounts, or the account's public key for the first block.</p>
     * <p><strong>CAUTION:</strong> This method will generate the work on the CPU. For GPU calculations, use the
     * work generation utility provided by the node through RPC
     * ({@link RequestWorkGenerate}).</p>
     * @param root      the root hash (64 character hex string)
     * @param threshold the minimum difficulty threshold
     * @return the generated work solution
     */
    public static WorkSolution generate(String root, WorkDifficulty threshold) {
        if (root == null) throw new IllegalArgumentException("Root argument cannot be null.");
        if (!JNanoHelper.isValidHex(root, 64))
            throw new IllegalArgumentException("Root argument must be a 64-character hex string.");
        
        return generate(JNanoHelper.ENCODER_HEX.decode(root), threshold);
    }
    
    /**
     * <p>Generates a work solution from the given root and minimum difficulty threshold. The root value should be
     * either the block hash for existing accounts, or the account's public key for the first block.</p>
     * <p><strong>CAUTION:</strong> This method will generate the work on the CPU. For GPU calculations, use the
     * work generation utility provided by the node through RPC
     * ({@link RequestWorkGenerate}).</p>
     * @param root      the root bytes (32 element byte array)
     * @param threshold the minimum difficulty threshold
     * @return the generated work solution
     */
    public static WorkSolution generate(byte[] root, WorkDifficulty threshold) {
        if (root == null) throw new IllegalArgumentException("Root array cannot be null.");
        if (root.length != 32) throw new IllegalArgumentException("Root array must have a length of 32.");
        if (threshold == null) throw new IllegalArgumentException("Difficulty threshold cannot be null.");
        
        byte[] thresholdBytes = convertLongToBytes(threshold.getAsLong());
        byte[] work = new byte[8];
        new Random().nextBytes(work); // Populate initial work array with random bytes
    
        Blake2b digest = new Blake2b(null, 8, null, null);
        byte[] difficulty = new byte[8];
        
        while (true) {
            digest.reset();
            digest.update(work, 0, work.length);
            digest.update(root, 0, root.length);
            digest.digest(difficulty, 0);
            
            // Compare threshold
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
        return JNanoHelper.reverseArray(JNanoHelper.longToBytes(val));
    }
    
    private static long convertBytesToLong(byte[] val) {
        return JNanoHelper.bytesToLong(JNanoHelper.reverseArray(val));
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
