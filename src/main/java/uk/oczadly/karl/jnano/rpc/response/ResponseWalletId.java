/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import uk.oczadly.karl.jnano.internal.gsonadapters.SingleValueJsonAdapter;
import uk.oczadly.karl.jnano.model.HexData;

/**
 * This response class contains a single wallet ID string.
 */
@JsonAdapter(SingleValueJsonAdapter.class)
public class ResponseWalletId extends RpcResponse {
    
    @Expose
    private HexData wallet;
    
    
    /**
     * @return the wallet's ID
     */
    public HexData getWalletId() {
        return wallet;
    }
    
}
