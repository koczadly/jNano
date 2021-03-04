/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.exception;

/**
 * <p>Thrown when a valid request is sent, but is disabled on the node.</p>
 */
public class RpcCommandNotAllowedException extends RpcFeatureDisabledException {
    
    public RpcCommandNotAllowedException(String message, String nodeMessage) {
        super(message, nodeMessage);
    }
    
    public RpcCommandNotAllowedException(String nodeMessage) {
        super(nodeMessage);
    }
    
}
