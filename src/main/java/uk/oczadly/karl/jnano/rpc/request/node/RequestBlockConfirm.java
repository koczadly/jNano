/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

/**
 * This request class is used to request confirmation for the specified block.
 * <br>Calls the RPC command {@code block_confirm}, and returns a {@link ResponseSuccessful} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#block_confirm">Official RPC documentation</a>
 */
public class RequestBlockConfirm extends RpcRequest<ResponseSuccessful> {
    
    @Expose @SerializedName("hash")
    private final String blockHash;
    
    
    /**
     * @param blockHash the block's hash
     */
    public RequestBlockConfirm(String blockHash) {
        super("block_confirm", ResponseSuccessful.class);
        this.blockHash = blockHash;
    }
    
    
    /**
     * @return the requested block's hash
     */
    public String getBlockHash() {
        return blockHash;
    }
    
}
