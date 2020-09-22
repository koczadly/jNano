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
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockPrevious;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockRepresentative;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a {@code change} block, and the associated data.
 *
 * <p>Note that this is a legacy block and has since been officially deprecated. For new blocks, use
 * {@link StateBlock state} blocks.</p>
 */
public class ChangeBlock extends Block implements IBlockPrevious, IBlockRepresentative {
    
    /** A function which converts a {@link JsonObject} into a {@link ChangeBlock} instance. */
    public static final Function<JsonObject, ChangeBlock> DESERIALIZER = json -> new ChangeBlock(
            JNH.getJson(json, "signature"),
            JNH.getJson(json, "work", WorkSolution::new),
            JNH.getJson(json, "previous"),
            JNH.getJson(json, "representative", NanoAccount::parseAddress));
    
    private static final BlockIntent INTENT = new BlockIntent(false, false, true, false, false, false);
    
    
    @Expose @SerializedName("previous")
    private final String previousBlockHash;
    
    @Expose @SerializedName("representative")
    private final NanoAccount representativeAccount;
    
    
    /**
     * Constructs a change block.
     * @param signature
     * @param workSolution
     * @param previousBlockHash
     * @param representativeAccount
     */
    public ChangeBlock(String signature, WorkSolution workSolution, String previousBlockHash,
                       NanoAccount representativeAccount) {
        super(BlockType.CHANGE, signature, workSolution);
    
        if (previousBlockHash == null) throw new IllegalArgumentException("Previous block hash cannot be null.");
        if (!JNH.isValidHex(previousBlockHash, HASH_LENGTH))
            throw new IllegalArgumentException("Previous block hash is invalid.");
        if (representativeAccount == null) throw new IllegalArgumentException("Block representative cannot be null.");
        
        this.previousBlockHash = previousBlockHash;
        this.representativeAccount = representativeAccount;
    }
    
    
    @Override
    public final String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
    @Override
    public final NanoAccount getRepresentative() {
        return representativeAccount;
    }
    
    @Override
    public BlockIntent getIntent() {
        return INTENT;
    }
    
    @Override
    public boolean contentEquals(Block block) {
        if (!(block instanceof ChangeBlock)) return false;
        ChangeBlock cb = (ChangeBlock)block;
        return super.contentEquals(cb)
                && Objects.equals(getPreviousBlockHash(), cb.getPreviousBlockHash())
                && Objects.equals(getRepresentative(), cb.getRepresentative());
    }
    
    
    @Override
    protected byte[][] generateHashables() {
        return new byte[][] {
                JNH.ENC_16.decode(getPreviousBlockHash()),
                getRepresentative().getPublicKeyBytes()
        };
    }
    
    
    /**
     * Parses a {@code change} block from a given JSON string using the default deserializer.
     * @param json the JSON data to parse from
     * @return a new {@link ChangeBlock} constructed from the given JSON data
     * @throws BlockDeserializer.BlockParseException if the block cannot be correctly parsed
     * @see BlockDeserializer
     * @see Block#parse(String)
     */
    public static ChangeBlock parse(String json) {
        return parse(JsonParser.parseString(json).getAsJsonObject());
    }
    
    /**
     * Parses a {@code change} block from a given {@link JsonObject} instance using the default deserializer.
     * @param json the JSON data to parse from
     * @return a new {@link ChangeBlock} constructed from the given JSON data
     * @throws BlockDeserializer.BlockParseException if the block cannot be correctly parsed
     * @see BlockDeserializer
     * @see Block#parse(JsonObject)
     */
    public static ChangeBlock parse(JsonObject json) {
        Block b = Block.parse(json);
        try {
            return (ChangeBlock)b;
        } catch (ClassCastException e) {
            throw new BlockDeserializer.BlockParseException("Block is not a change block.", e);
        }
    }
   
}
