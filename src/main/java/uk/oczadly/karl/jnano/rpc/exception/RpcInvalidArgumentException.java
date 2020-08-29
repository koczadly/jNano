/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.exception;

/**
 * Thrown if one of the given request arguments are not valid.
 */
public class RpcInvalidArgumentException extends RpcException {
    
    public RpcInvalidArgumentException(String message) {
        super(message);
    }
    
}
