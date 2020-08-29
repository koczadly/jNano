/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseValidation;

/**
 * This request class is used to check whether a wallet's password is valid.
 * <br>Calls the RPC command {@code password_valid}, and returns a {@link ResponseValidation} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#password_valid">Official RPC documentation</a>
 */
public class RequestPasswordValid extends RpcRequest<ResponseValidation> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    
    /**
     * @param walletId the wallet's ID
     */
    public RequestPasswordValid(String walletId) {
        super("password_valid", ResponseValidation.class);
        this.walletId = walletId;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
}
