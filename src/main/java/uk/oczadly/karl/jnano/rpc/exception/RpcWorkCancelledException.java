/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.exception;

/**
 * Thrown when a work generation request is cancelled before it could be completed.
 */
public class RpcWorkCancelledException extends RpcException {
    
    public RpcWorkCancelledException() {
        this("The work generation request was cancelled.");
    }
    
    public RpcWorkCancelledException(String message) {
        super(message);
    }
    
}
