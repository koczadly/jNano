/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlocksMap;

/**
 * This request class is used to fetch a list of unchecked blocks.
 * <br>Calls the RPC command {@code unchecked}, and returns a {@link ResponseBlocksMap} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#unchecked">Official RPC documentation</a>
 */
public class RequestUnchecked extends RpcRequest<ResponseBlocksMap> {
    
    @Expose @SerializedName("count")
    private final int count;
    
    
    /**
     * @param count the result limit
     */
    public RequestUnchecked(int count) {
        super("unchecked", ResponseBlocksMap.class);
        this.count = count;
    }
    
    
    /**
     * @return the requested result limit
     */
    public int getCount() {
        return count;
    }
    
}
