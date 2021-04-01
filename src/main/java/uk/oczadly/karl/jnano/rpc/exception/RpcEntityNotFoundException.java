/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.exception;

/**
 * Thrown if one of the referenced entities (eg. account, wallet, block hash) do not exist.
 */
public class RpcEntityNotFoundException extends RpcInvalidArgumentException {
    
    public RpcEntityNotFoundException(String rawMessage) {
        super(rawMessage);
    }
    
}
