/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAmount;


/**
 * This response class contains a single amount of Nano.
 */
public class ResponseAmount extends RpcResponse {
    
    @Expose @SerializedName(value = "amount", alternate = {"weight", "available"})
    private NanoAmount amount;
    
    
    /**
     * @return the amount of Nano
     */
    public NanoAmount getAmount() {
        return amount;
    }
    
}
