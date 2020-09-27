/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAccount;

/**
 * This response class contains information given when changing a wallet's seed.
 */
public class ResponseWalletChangeSeed extends RpcResponse {
    
    @Expose @SerializedName("last_restored_account")
    private NanoAccount lastAccount;
    
    @Expose @SerializedName("restored_count")
    private int restoredCount;
    
    
    /**
     * @return the address of the last account to be restored
     */
    public NanoAccount getLastAccount() {
        return lastAccount;
    }
    
    /**
     * @return the number of accounts restored
     */
    public int getRestoredCount() {
        return restoredCount;
    }
    
}
