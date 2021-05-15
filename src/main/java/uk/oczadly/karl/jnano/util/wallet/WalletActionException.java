/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.wallet;

import uk.oczadly.karl.jnano.rpc.exception.RpcException;

/**
 * Thrown when an error occurs with an RPC-related wallet activity.
 *
 * <p>This exception may wrap the {@link RpcException} and {@link java.io.IOException} exceptions thrown by the RPC
 * client.</p>
 */
public class WalletActionException extends Exception {
    
    public WalletActionException(String message) {
        super(message);
    }
    
    public WalletActionException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public WalletActionException(Throwable cause) {
        super(cause);
    }
    
}
