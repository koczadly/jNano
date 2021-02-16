/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This response class contains a single numerical count.
 */
public class ResponseCount extends RpcResponse {
    
    @Expose @SerializedName("count")
    private long count;
    
    
    /**
     * @return the number, count or quantity
     */
    public long getCount() {
        return count;
    }
    
}
