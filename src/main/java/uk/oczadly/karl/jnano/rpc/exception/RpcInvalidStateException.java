/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.exception;

/**
 * Thrown if the state of a request object is invalid. Example causes: outdated block parameters (eg. wrong previous or
 * balance) or fewer funds than requesting send amount.
 */
public class RpcInvalidStateException extends RpcInvalidArgumentException {
    
    public RpcInvalidStateException(String rawMessage) {
        super(rawMessage);
    }
    
}
