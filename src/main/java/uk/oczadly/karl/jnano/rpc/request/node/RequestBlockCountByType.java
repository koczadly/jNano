/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockCountByType;

/**
 * This request class is used to request the number of blocks in the ledger, sorted by type.
 * <br>Calls the RPC command {@code block_count_type}, and returns a {@link ResponseBlockCountByType} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#block_count_type">Official RPC documentation</a>
 */
public class RequestBlockCountByType extends RpcRequest<ResponseBlockCountByType> {
    
    public RequestBlockCountByType() {
        super("block_count_type", ResponseBlockCountByType.class);
    }
    
}
