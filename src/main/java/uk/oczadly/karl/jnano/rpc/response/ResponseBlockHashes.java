/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.HexData;

import java.util.Set;

/**
 * This response class contains a series of block hashes.
 */
public class ResponseBlockHashes extends RpcResponse {
    
    @Expose @SerializedName(value = "blocks", alternate = "hashes")
    private Set<HexData> blockHashes;
    
    
    /**
     * @return a set of block hashes
     */
    public Set<HexData> getBlockHashes() {
        return blockHashes;
    }
    
}
