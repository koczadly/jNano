/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccount;

/**
 * This request class is used to request the account which contains the specified block.
 * <br>Calls the RPC command {@code block_account}, and returns a {@link ResponseAccount} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#block_account">Official RPC documentation</a>
 */
public class RequestBlockAccount extends RpcRequest<ResponseAccount> {
    
    @Expose @SerializedName("hash")
    private final String blockHash;
    
    
    /**
     * @param blockHash the block's hash
     */
    public RequestBlockAccount(String blockHash) {
        super("block_account", ResponseAccount.class);
        this.blockHash = blockHash;
    }
    
    
    /**
     * @return the requested block's hash
     */
    public String getBlockHash() {
        return blockHash;
    }
    
}
