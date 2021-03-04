/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.exception;

import uk.oczadly.karl.jnano.rpc.RpcQueryNode;

/**
 * Thrown when an unhandled exception occurs internally within the {@link RpcQueryNode} or the assigned RPC
 * serializer/deserializer/executor objects.
 */
public class RpcUnhandledException extends RpcException {
    
    public RpcUnhandledException(String message, Exception cause) {
        super(message, cause);
    }
    
}
