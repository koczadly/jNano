/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAmount;


/**
 * This response class contains both pocketed and pending balance amounts.
 */
public class ResponseBalance extends RpcResponse {
    
    @Expose @SerializedName("balance")
    private NanoAmount pocketed;
    
    @Expose @SerializedName("pending")
    private NanoAmount pending;
    
    
    /**
     * @return the total balance (pocketed)
     */
    public NanoAmount getPocketed() {
        return pocketed;
    }
    
    /**
     * @return the total balance from pending blocks
     */
    public NanoAmount getPending() {
        return pending;
    }
    
    /**
     * @return the total balance of the account
     */
    public NanoAmount getTotal() {
        return new NanoAmount(pending.getAsRaw().add(pocketed.getAsRaw()));
    }
    
}
