/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.HexData;

/**
 * This response class contains a single String representing a key, either public or private.
 */
public class ResponseKey extends RpcResponse {
    
    @Expose @SerializedName("key")
    private HexData key;
    
    
    /**
     * @return the key
     */
    public HexData getKey() {
        return key;
    }
    
}
