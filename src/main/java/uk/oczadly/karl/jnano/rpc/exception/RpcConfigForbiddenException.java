/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.exception;

/**
 * Thrown if a requested command could not be processed due to one or more node configuration parameters prohibiting it.
 */
public class RpcConfigForbiddenException extends RpcException {
    
    public RpcConfigForbiddenException(String nodeMessage) {
        super(null, nodeMessage);
    }
    
    public RpcConfigForbiddenException(String message, String nodeMessage) {
        super(message, nodeMessage);
    }
    
}
