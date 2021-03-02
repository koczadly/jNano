/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.exception;

/**
 * Thrown when a query needs access to a wallet which is locked.
 */
public class RpcWalletLockedException extends RpcExternalException {
    
    public RpcWalletLockedException(String rawMessage) {
        super("The accessed wallet is currently locked.", rawMessage);
    }
    
}
