/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.exception;

/**
 * Thrown when a request is cancelled before it could be completed.
 */
public class RpcRequestCancelledException extends RpcException {
    
    public RpcRequestCancelledException() {
        this("The request was cancelled.");
    }
    
    public RpcRequestCancelledException(String message) {
        super(message);
    }
    
}
