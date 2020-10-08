/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.block.Block;

/**
 * This response class contains a block and an accompanying signature.
 */
public class ResponseSignature extends RpcResponse {
    
    @Expose @SerializedName("block")
    private Block block;
    
    @Expose @SerializedName("signature")
    private HexData signature;
    
    
    /**
     * @return the contents of the block
     */
    public Block getBlock() {
        return block;
    }
    
    /**
     * @return the signature of the block
     */
    public HexData getSignature() {
        return signature;
    }
    
}
