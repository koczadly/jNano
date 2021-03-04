/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.exception;

/**
 * Thrown if the node encounters an internal error.
 */
public class RpcNodeInternalErrorException extends RpcExternalException {
    
    public RpcNodeInternalErrorException(String rawMessage) {
        super(rawMessage);
    }
    
    public RpcNodeInternalErrorException(String message, String rawMessage) {
        super(message, rawMessage);
    }
    
}
