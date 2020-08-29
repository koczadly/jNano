/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This response class contains a series of block hashes.
 */
public class ResponseBlockConfirmations extends ResponseBlockHashes {
    
    @Expose @SerializedName("unconfirmed")
    private int unconfirmed;
    
    @Expose @SerializedName("confirmed")
    private int confirmed;
    
    
    public int getUnconfirmed() {
        return unconfirmed;
    }
    
    public int getConfirmed() {
        return confirmed;
    }
    
}
