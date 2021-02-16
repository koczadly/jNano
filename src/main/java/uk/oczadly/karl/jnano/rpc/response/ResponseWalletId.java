/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.HexData;

/**
 * This response class contains a single wallet ID string.
 */
public class ResponseWalletId extends RpcResponse {
    
    @Expose @SerializedName("wallet")
    private HexData wallet;
    
    
    /**
     * @return the wallet's ID
     */
    public HexData getWalletId() {
        return wallet;
    }
    
}
