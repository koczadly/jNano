/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseMultiAccountBalances;

/**
 * This request class is used to request the balances of multiple specified accounts.
 * <br>Calls the RPC command {@code accounts_balances}, and returns a {@link ResponseMultiAccountBalances} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#accounts_balances">Official RPC documentation</a>
 */
public class RequestMultiAccountBalances extends RpcRequest<ResponseMultiAccountBalances> {
    
    @Expose @SerializedName("accounts")
    private final String[] accounts;
    
    
    /**
     * @param accounts the accounts' addresses
     */
    public RequestMultiAccountBalances(String... accounts) {
        super("accounts_balances", ResponseMultiAccountBalances.class);
        this.accounts = accounts;
    }
    
    
    /**
     * @return the requested accounts
     */
    public String[] getAccounts() {
        return accounts;
    }
    
}
