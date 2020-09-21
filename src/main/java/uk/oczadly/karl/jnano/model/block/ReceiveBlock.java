/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockPrevious;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockSource;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a {@code receive} block, and the associated data.
 *
 * <p>Note that this is a legacy block and has since been officially deprecated. For new blocks, use
 * {@link StateBlock state} blocks.</p>
 */
public class ReceiveBlock extends Block implements IBlockPrevious, IBlockSource {
    
    /** A function which converts a {@link JsonObject} into a {@link ReceiveBlock} instance. */
    public static final Function<JsonObject, ReceiveBlock> DESERIALIZER = json -> new ReceiveBlock(
            json.get("signature").getAsString(),
            JNH.nullable(json.get("work"), o -> new WorkSolution(o.getAsString())),
            json.get("previous").getAsString(),
            json.get("source").getAsString());
    
    private static final BlockIntent INTENT = new BlockIntent(false, true, false, false, false, false);
    
    
    @Expose @SerializedName("previous")
    private String previousBlockHash;
    
    @Expose @SerializedName("source")
    private String sourceBlockHash;
    
    
    ReceiveBlock() {
        super(BlockType.RECEIVE);
    }
    
    public ReceiveBlock(String signature, WorkSolution work, String previousBlockHash, String sourceBlockHash) {
        super(BlockType.RECEIVE, signature, work);
    
        if (previousBlockHash == null) throw new IllegalArgumentException("Previous block hash cannot be null.");
        if (!JNH.isValidHex(previousBlockHash, HASH_LENGTH))
            throw new IllegalArgumentException("Previous block hash is invalid.");
        if (sourceBlockHash == null) throw new IllegalArgumentException("Source block hash cannot be null.");
        if (!JNH.isValidHex(sourceBlockHash, HASH_LENGTH))
            throw new IllegalArgumentException("Source block hash is invalid.");
        
        this.previousBlockHash = previousBlockHash;
        this.sourceBlockHash = sourceBlockHash;
    }
    
    
    @Override
    public final String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
    @Override
    public final String getSourceBlockHash() {
        return sourceBlockHash;
    }
    
    @Override
    public BlockIntent getIntent() {
        return INTENT;
    }
    
    @Override
    public boolean contentEquals(Block block) {
        if (!(block instanceof ReceiveBlock)) return false;
        ReceiveBlock rb = (ReceiveBlock)block;
        return super.contentEquals(rb)
                && Objects.equals(getPreviousBlockHash(), rb.getPreviousBlockHash())
                && Objects.equals(getSourceBlockHash(), rb.getSourceBlockHash());
    }
    
    
    @Override
    protected byte[][] generateHashables() {
        return new byte[][] {
                JNH.ENC_16.decode(getPreviousBlockHash()),
                JNH.ENC_16.decode(getSourceBlockHash())
        };
    }
    
    
    /**
     * Parses a block from a given JSON string using the default deserializer.
     * @param json the json to parse from
     * @return a block object derived from the provided JSON
     * @throws BlockDeserializer.BlockParseException if the block cannot be correctly parsed
     * @see BlockDeserializer
     * @see Block#parse(String)
     */
    public static ReceiveBlock parse(String json) {
        return parse(JsonParser.parseString(json).getAsJsonObject());
    }
    
    /**
     * Parses a block from a given {@link JsonObject} instance using the default deserializer.
     * @param json the json to parse from
     * @return a block object derived from the provided JSON
     * @throws BlockDeserializer.BlockParseException if the block cannot be correctly parsed
     * @see BlockDeserializer
     * @see Block#parse(JsonObject)
     */
    public static ReceiveBlock parse(JsonObject json) {
        Block b = Block.parse(json);
        try {
            return (ReceiveBlock)b;
        } catch (ClassCastException e) {
            throw new BlockDeserializer.BlockParseException("Block is not a receive block.", e);
        }
    }
    
}
