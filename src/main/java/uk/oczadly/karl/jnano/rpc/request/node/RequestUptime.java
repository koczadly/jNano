/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseUptime;

/**
 * This request class is used to retrieve the current uptime of the node.
 * <br>Calls the RPC command {@code uptime}, and returns a {@link ResponseUptime} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#uptime">Official RPC documentation</a>
 */
public class RequestUptime extends RpcRequest<ResponseUptime> {
    
    public RequestUptime() {
        super("uptime", ResponseUptime.class);
    }
    
}
