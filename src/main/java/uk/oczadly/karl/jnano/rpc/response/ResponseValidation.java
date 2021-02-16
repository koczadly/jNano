/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This response class contains a single boolean which represents if something is valid.
 */
public class ResponseValidation extends RpcResponse {
    
    @Expose @SerializedName("valid")
    private boolean isValid;
    
    
    /**
     * @return whether the request was valid
     */
    public boolean isValid() {
        return isValid;
    }
    
}
