/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.exception;

/**
 * The default base RPC exception, thrown when there is a problem with an RPC request or the remote node.
 */
public abstract class RpcException extends Exception {
    
    public RpcException(String message) {
        super(message);
    }
    
    public RpcException(String message, Exception cause) {
        super(message, cause);
    }
    
    
    @Override
    public synchronized Exception getCause() {
        return (Exception)super.getCause();
    }
    
    @Override
    public final synchronized Throwable initCause(Throwable cause) {
        if (!(cause instanceof Exception))
            throw new IllegalArgumentException("Cause must extend Exception.");
        return super.initCause(cause);
    }
    
}
