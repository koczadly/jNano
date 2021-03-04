/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.exception;

/**
 * Thrown if the node encounters an internal error.
 */
public class RpcInternalErrorException extends RpcExternalException {
    
    public RpcInternalErrorException(String rawMessage) {
        super(rawMessage);
    }
    
    public RpcInternalErrorException(String message, String rawMessage) {
        super(message, rawMessage);
    }
    
}
