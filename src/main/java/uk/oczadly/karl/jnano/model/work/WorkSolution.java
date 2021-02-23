/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.work;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.internal.NanoConst;
import uk.oczadly.karl.jnano.internal.utils.NanoUtil;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockAccount;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockPrevious;
import uk.oczadly.karl.jnano.util.workgen.WorkGenerator;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * This class represents a proof-of-work solution.
 *
 * @see WorkGenerator
 */
@JsonAdapter(WorkSolution.WorkSolutionJsonAdapter.class)
public final class WorkSolution {
    
    private final long longVal;
    private final String hexVal;
    
    /**
     * @param hexVal the work solution, encoded as a hexadecimal string, eg: {@code d1075495f302e300}
     */
    public WorkSolution(String hexVal) {
        if (hexVal.length() != 16)
            throw new IllegalArgumentException("Difficulty must be a 16-character hex string.");
        this.longVal = Long.parseUnsignedLong(hexVal, 16);
        this.hexVal = hexVal.toLowerCase();
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
    public WorkDifficulty calculateDifficulty(HexData root) {
        if (root == null) throw new IllegalArgumentException("Root hash cannot be null.");
        if (root.length() != NanoConst.LEN_HASH_B)
            throw new IllegalArgumentException("Root hash must be a 64-character hex value.");
        
        return calculateDifficulty(root.toByteArray());
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
    
        byte[] difficultyBytes = JNH.blake2b(8, longToBytes(longVal), root);
        return new WorkDifficulty(bytesToLong(difficultyBytes));
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
     *
     * @param block the block to calculate the root of
     * @return the root hash of the given block
     * @throws IllegalArgumentException if the block does not contain a {@code previous} or {@code account} field
     */
    public static HexData getRoot(Block block) {
        return NanoUtil.getWorkRoot(block);
    }
    
    
    private static byte[] longToBytes(long val) {
        return JNH.reverseArray(JNH.longToBytes(val));
    }
    
    private static long bytesToLong(byte[] val) {
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
