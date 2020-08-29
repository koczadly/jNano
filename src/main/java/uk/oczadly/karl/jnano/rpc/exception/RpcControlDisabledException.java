/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.exception;

/**
 * <p>Thrown when a request needs control access but it's disabled on the node.</p>
 * <p>Set {@code enable_control = true} in the node's configuration to prevent this error.</p>
 */
public class RpcControlDisabledException extends RpcFeatureDisabledException {
    
    public RpcControlDisabledException() {
        super("The specified command requires control to be enabled on the RPC server.");
    }
    
}
