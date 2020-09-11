/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockPrevious;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockRepresentative;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.util.function.Function;

/**
 * Represents a {@code change} block, and the associated data.
 */
public class ChangeBlock extends Block implements IBlockPrevious, IBlockRepresentative {
    
    /** A function which converts a {@link JsonObject} into a {@link ChangeBlock} instance. */
    public static final Function<JsonObject, ChangeBlock> DESERIALIZER = json -> new ChangeBlock(
            JNH.nullable(json.get("hash"), JsonElement::getAsString),
            json.get("signature").getAsString(),
            JNH.nullable(json.get("work"), o -> new WorkSolution(o.getAsString())),
            json.get("previous").getAsString(),
            NanoAccount.parseAddress(json.get("representative").getAsString()));
    
    private static final BlockIntent INTENT = new BlockIntent(false, false, true, false, false, false);
    
    
    @Expose @SerializedName("previous")
    private String previousBlockHash;
    
    @Expose @SerializedName("representative")
    private NanoAccount representativeAccount;
    
    
    ChangeBlock() {
        super(BlockType.CHANGE);
    }
    
    public ChangeBlock(String signature, WorkSolution workSolution, String previousBlockHash,
                       NanoAccount representativeAccount) {
        this(null, signature, workSolution, previousBlockHash, representativeAccount);
    }
    
    protected ChangeBlock(String hash, String signature, WorkSolution workSolution,
                       String previousBlockHash, NanoAccount representativeAccount) {
        super(BlockType.CHANGE, hash, signature, workSolution);
    
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
    protected byte[][] generateHashables() {
        return new byte[][] {
                JNH.ENC_16.decode(getPreviousBlockHash()),
                getRepresentative().getPublicKeyBytes()
        };
    }
   
}
