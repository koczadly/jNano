/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseKey;

/**
 * This request class is used to convert an account's address into a public key.
 * <br>Calls the RPC command {@code account_key}, and returns a {@link ResponseKey} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#account_key">Official RPC documentation</a>
 *
 * @deprecated Use of built-in {@link NanoAccount#toPublicKey()} is preferred.
 */
@Deprecated
public class RequestAccountPublicKey extends RpcRequest<ResponseKey> {
    
    @Expose @SerializedName("account")
    private final String account;
    
    
    /**
     * @param account the account's address
     */
    public RequestAccountPublicKey(String account) {
        super("account_key", ResponseKey.class);
        this.account = account;
    }
    
    
    /**
     * @return the requested account's address
     */
    public String getAccount() {
        return account;
    }
    
}
