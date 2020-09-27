/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWalletId;

/**
 * This request class is used to create a new local wallet.
 * <br>Calls the RPC command {@code wallet_create}, and returns a {@link ResponseWalletId} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#wallet_create">Official RPC documentation</a>
 */
public class RequestWalletCreate extends RpcRequest<ResponseWalletId> {
    
    @Expose @SerializedName("seed")
    private final String seed;
    
    
    public RequestWalletCreate() {
        this(null);
    }
    
    /**
     * @param seed the seed for the wallet to use
     */
    public RequestWalletCreate(String seed) {
        super("wallet_create", ResponseWalletId.class);
        this.seed = seed;
    }
    
    
    /**
     * @return the seed for the wallet to use
     */
    public String getSeed() {
        return seed;
    }
    
}
