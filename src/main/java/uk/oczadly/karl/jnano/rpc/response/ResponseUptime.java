/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This response class contains the number of seconds the node has been online for.
 */
public class ResponseUptime extends RpcResponse {
    
    @Expose @SerializedName("seconds")
    private int seconds;
    
    
    /**
     * @return the current uptime, in seconds
     */
    public int getUptimeSeconds() {
        return seconds;
    }
    
}
