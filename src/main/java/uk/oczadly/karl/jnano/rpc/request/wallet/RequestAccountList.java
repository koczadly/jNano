/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccounts;

/**
 * This request class is used to fetch a list of accounts inside in the given wallet.
 * <br>Calls the RPC command {@code account_list}, and returns a {@link ResponseAccounts} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#account_list">Official RPC documentation</a>
 */
public class RequestAccountList extends RpcRequest<ResponseAccounts> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    
    /**
     * @param walletId the wallet's ID
     */
    public RequestAccountList(String walletId) {
        super("account_list", ResponseAccounts.class);
        this.walletId = walletId;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
}
