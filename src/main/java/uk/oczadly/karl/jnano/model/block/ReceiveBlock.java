/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.internal.NanoConst;
import uk.oczadly.karl.jnano.model.HexData;
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
            (HexData)JNH.getJson(json, "signature", HexData::new),
            JNH.getJson(json, "work", WorkSolution::new),
            JNH.getJson(json, "previous", HexData::new),
            JNH.getJson(json, "source", HexData::new));
    
    private static final BlockIntent INTENT = new BlockIntent(false, true, false, false, false, false);
    
    
    @Expose @SerializedName("previous")
    private final HexData previousBlockHash;
    
    @Expose @SerializedName("source")
    private final HexData sourceBlockHash;
    
    
    @Deprecated(forRemoval = true)
    public ReceiveBlock(String signature, WorkSolution work, String previousBlockHash, String sourceBlockHash) {
        this(new HexData(signature), work, new HexData(previousBlockHash), new HexData(sourceBlockHash));
    }
    
    public ReceiveBlock(HexData signature, WorkSolution work, HexData previousBlockHash, HexData sourceBlockHash) {
        super(BlockType.RECEIVE, signature, work);
    
        if (previousBlockHash == null) throw new IllegalArgumentException("Previous block hash cannot be null.");
        if (!JNH.isValidLength(previousBlockHash, NanoConst.LEN_HASH_B))
            throw new IllegalArgumentException("Previous block hash is an invalid length.");
        if (sourceBlockHash == null) throw new IllegalArgumentException("Source block hash cannot be null.");
        if (!JNH.isValidLength(sourceBlockHash, NanoConst.LEN_HASH_B))
            throw new IllegalArgumentException("Source block hash is an invalid length.");
        
        this.previousBlockHash = previousBlockHash;
        this.sourceBlockHash = sourceBlockHash;
    }
    
    
    @Override
    public final HexData getPrevHash() {
        return previousBlockHash;
    }
    
    @Override
    public final HexData getSourceBlockHash() {
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
                && Objects.equals(getPrevHash(), rb.getPrevHash())
                && Objects.equals(getSourceBlockHash(), rb.getSourceBlockHash());
    }
    
    
    @Override
    protected byte[][] generateHashables() {
        return new byte[][] {
                getPrevHash().toByteArray(),
                getSourceBlockHash().toByteArray()
        };
    }
    
    
    /**
     * Parses a {@code receive} block from a given JSON string using the default deserializer.
     * @param json the JSON data to parse from
     * @return a new {@link ReceiveBlock} constructed from the given JSON data
     * @throws BlockDeserializer.BlockParseException if the block cannot be correctly parsed
     * @see BlockDeserializer
     * @see Block#parse(String)
     */
    public static ReceiveBlock parse(String json) {
        return JNH.tryRethrow(Block.parse(json), b -> (ReceiveBlock)b,
                e -> new BlockDeserializer.BlockParseException("Block is not a receive block.", e));
    }
    
    /**
     * Parses a {@code receive} block from a given {@link JsonObject} instance using the default deserializer.
     * @param json the JSON data to parse from
     * @return a new {@link ReceiveBlock} constructed from the given JSON data
     * @throws BlockDeserializer.BlockParseException if the block cannot be correctly parsed
     * @see BlockDeserializer
     * @see Block#parse(JsonObject)
     */
    public static ReceiveBlock parse(JsonObject json) {
        return JNH.tryRethrow(Block.parse(json), b -> (ReceiveBlock)b,
                e -> new BlockDeserializer.BlockParseException("Block is not a receive block.", e));
    }
    
}
