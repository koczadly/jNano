/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

/**
 * This request class is used to remove all the configured work peers from the node.
 * <br>Calls the RPC command {@code work_peers_clear}, and returns a {@link ResponseSuccessful} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#work_peers_clear">Official RPC documentation</a>
 */
public class RequestWorkPeersClear extends RpcRequest<ResponseSuccessful> {
    
    public RequestWorkPeersClear() {
        super("work_peers_clear", ResponseSuccessful.class);
    }
    
}
