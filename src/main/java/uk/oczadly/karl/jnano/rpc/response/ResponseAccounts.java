/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAccount;

import java.util.Set;

/**
 * This response class contains a set of account addresses.
 */
public class ResponseAccounts extends RpcResponse {
    
    @Expose @SerializedName("accounts")
    private Set<NanoAccount> accounts;
    
    
    /**
     * @return a set of account addresses
     */
    public Set<NanoAccount> getAccounts() {
        return accounts;
    }
    
}
