/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.exception;

/**
 * Thrown if the node throws an error which could not be classified into an existing {@link RpcException} subtype.
 */
public class RpcUnrecognizedException extends RpcExternalException {
    
    public RpcUnrecognizedException(String rawMessage) {
        super(rawMessage);
    }
    
}
