/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.exception;

/**
 * Thrown if the node encounters an internal error.
 */
public class RpcInternalException extends RpcException {
    
    public RpcInternalException(String nodeMessage) {
        super(null, nodeMessage);
    }
    
    public RpcInternalException(String message, String nodeMessage) {
        super(message, nodeMessage);
    }
    
}
