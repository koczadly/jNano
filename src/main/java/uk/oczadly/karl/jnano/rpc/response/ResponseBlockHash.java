/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.HexData;

/**
 * This response class contains a single block hash.
 */
public class ResponseBlockHash extends RpcResponse {
    
    @Expose @SerializedName(value = "hash", alternate = "block")
    private HexData blockHash;
    
    
    /**
     * @return the block's hash
     */
    public HexData getBlockHash() {
        return blockHash;
    }
    
}
