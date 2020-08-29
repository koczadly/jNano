/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlock;

/**
 * This request class is used to fetch an unchecked block by its hash.
 * <br>Calls the RPC command {@code unchecked_get}, and returns a {@link ResponseBlock} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#unchecked_get">Official RPC documentation</a>
 */
public class RequestUncheckedGet extends RpcRequest<ResponseBlock> {
    
    @Expose @SerializedName("json_block")
    private final boolean json = true;
    
    
    @Expose @SerializedName("hash")
    private final String blockHash;
    
    
    /**
     * @param blockHash the hash of the block
     */
    public RequestUncheckedGet(String blockHash) {
        super("unchecked_get", ResponseBlock.class);
        this.blockHash = blockHash;
    }
    
    
    /**
     * @return the requested block's hash
     */
    public String getBlockHash() {
        return blockHash;
    }
    
}
