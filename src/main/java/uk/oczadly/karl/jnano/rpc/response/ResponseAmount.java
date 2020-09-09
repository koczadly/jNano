/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import uk.oczadly.karl.jnano.internal.gsonadapters.SingleValueJsonAdapter;
import uk.oczadly.karl.jnano.model.NanoAmount;


/**
 * This response class contains a single amount of Nano.
 */
@JsonAdapter(SingleValueJsonAdapter.class)
public class ResponseAmount extends RpcResponse {
    
    @Expose
    private NanoAmount amount;
    
    
    /**
     * @return the amount of Nano in RAW
     */
    public NanoAmount getAmount() {
        return amount;
    }
    
}
