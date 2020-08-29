/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseMultiAccountFrontiers;

/**
 * This request class is used to fetch a list of accounts within a wallet, and their head block hashes.
 * <br>Calls the RPC command {@code wallet_frontiers}, and returns a {@link ResponseMultiAccountFrontiers} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#wallet_frontiers">Official RPC documentation</a>
 */
public class RequestWalletFrontiers extends RpcRequest<ResponseMultiAccountFrontiers> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    
    /**
     * @param walletId the wallet's ID
     */
    public RequestWalletFrontiers(String walletId) {
        super("wallet_frontiers", ResponseMultiAccountFrontiers.class);
        this.walletId = walletId;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
}
