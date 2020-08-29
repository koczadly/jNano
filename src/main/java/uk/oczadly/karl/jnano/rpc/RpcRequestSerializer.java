/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;

/**
 * Classes which implement this interface are responsible for serializing {@link RpcRequest} objects into string
 * values (typically JSON).
 */
public interface RpcRequestSerializer {
    
    /**
     * Serializes a given {@link RpcRequest} object into the expected string format. This string value is then
     * submitted to the node as the request body.
     * @param request the RPC request data
     * @return the serialized request
     */
    String serialize(RpcRequest<?> request);

}
