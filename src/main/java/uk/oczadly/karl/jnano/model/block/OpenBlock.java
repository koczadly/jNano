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
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockAccount;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockRepresentative;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockSource;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.util.function.Function;

/**
 * Represents an {@code open} block, and the associated data.
 *
 * <p>Note that this is a legacy block and has since been officially deprecated. For new blocks, use
 * {@link StateBlock state} blocks.</p>
 */
public class OpenBlock extends Block implements IBlockSource, IBlockAccount, IBlockRepresentative {
    
    /** A function which converts a {@link JsonObject} into a {@link OpenBlock} instance. */
    public static final Function<JsonObject, OpenBlock> DESERIALIZER = json -> new OpenBlock(
            json.get("signature").getAsString(),
            JNH.nullable(json.get("work"), o -> new WorkSolution(o.getAsString())),
            json.get("source").getAsString(),
            NanoAccount.parseAddress(json.get("account").getAsString()),
            NanoAccount.parseAddress(json.get("representative").getAsString()));
    
    private static final BlockIntent INTENT = new BlockIntent(false, true, true, true, false, false);
    private static final BlockIntent INTENT_GENESIS = new BlockIntent(false, true, true, true, false, true);
    
    
    @Expose @SerializedName("source")
    private String sourceBlockHash;
    
    @Expose @SerializedName("account")
    private NanoAccount accountAddress;
    
    @Expose @SerializedName("representative")
    private NanoAccount representativeAccount;
    
    
    OpenBlock() {
        super(BlockType.OPEN);
    }
    
    public OpenBlock(String signature, WorkSolution work, String sourceBlockHash, NanoAccount accountAddress,
                     NanoAccount representativeAccount) {
        super(BlockType.OPEN, signature, work);
    
        if (sourceBlockHash == null) throw new IllegalArgumentException("Source block hash cannot be null.");
        if (!JNH.isValidHex(sourceBlockHash, HASH_LENGTH))
            throw new IllegalArgumentException("Previous block hash is invalid.");
        if (accountAddress == null) throw new IllegalArgumentException("Block account cannot be null.");
        if (representativeAccount == null) throw new IllegalArgumentException("Block representative cannot be null.");
        
        this.sourceBlockHash = sourceBlockHash;
        this.accountAddress = accountAddress;
        this.representativeAccount = representativeAccount;
    }
    
    
    @Override
    public final String getSourceBlockHash() {
        return sourceBlockHash;
    }
    
    @Override
    public final NanoAccount getAccount() {
        return accountAddress;
    }
    
    @Override
    public final NanoAccount getRepresentative() {
        return representativeAccount;
    }
    
    @Override
    public BlockIntent getIntent() {
        if (getSourceBlockHash().equals(getAccount().toPublicKey()) && getAccount().equals(getRepresentative())) {
            return INTENT_GENESIS; // Genesis block special case
        }
        return INTENT;
    }
    
    
    @Override
    protected byte[][] generateHashables() {
        return new byte[][] {
                JNH.ENC_16.decode(getSourceBlockHash()),
                getRepresentative().getPublicKeyBytes(),
                getAccount().getPublicKeyBytes()
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
    public static OpenBlock parse(String json) {
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
    public static OpenBlock parse(JsonObject json) {
        Block b = Block.parse(json);
        try {
            return (OpenBlock)b;
        } catch (ClassCastException e) {
            throw new BlockDeserializer.BlockParseException("Block is not an open block.", e);
        }
    }
    
}
