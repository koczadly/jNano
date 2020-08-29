/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAmount;

/**
 * This request class is used to request the voting weight for the account.
 * <br>Calls the RPC command {@code account_weight}, and returns a {@link ResponseAmount} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#account_weight">Official RPC documentation</a>
 */
public class RequestAccountWeight extends RpcRequest<ResponseAmount> {
    
    @Expose @SerializedName("account")
    private final String account;
    
    
    /**
     * @param account the account's address
     */
    public RequestAccountWeight(String account) {
        super("account_weight", ResponseAmount.class);
        this.account = account;
    }
    
    
    /**
     * @return the requested account's address
     */
    public String getAccount() {
        return account;
    }
    
}
