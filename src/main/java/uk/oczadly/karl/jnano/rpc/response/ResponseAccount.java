/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAccount;

/**
 * This response class contains a single account address.
 */
public class ResponseAccount extends RpcResponse {
    
    @Expose @SerializedName(value = "account", alternate = "representative")
    private NanoAccount address;
    
    
    /**
     * @return the account's address
     */
    public NanoAccount getAccountAddress() {
        return address;
    }
    
}
