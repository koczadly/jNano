/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This response class contains a single boolean representing whether the wallet is locked.
 */
public class ResponseWalletLocked extends RpcResponse {
    
    @Expose @SerializedName("locked")
    private boolean isLocked;
    
    
    /**
     * @return true if the wallet is locked
     */
    public boolean isWalletLocked() {
        return isLocked;
    }
    
}
