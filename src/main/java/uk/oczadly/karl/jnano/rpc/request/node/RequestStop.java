/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

/**
 * This request class is used to shutdown the node.
 * <br>Calls the RPC command {@code stop}, and returns a {@link ResponseSuccessful} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#stop">Official RPC documentation</a>
 */
public class RequestStop extends RpcRequest<ResponseSuccessful> {
    
    public RequestStop() {
        super("stop", ResponseSuccessful.class);
    }
    
}
