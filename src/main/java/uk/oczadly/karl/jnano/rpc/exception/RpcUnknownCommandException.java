/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.exception;

/**
 * Thrown if the node does not understand the request command.
 */
//todo: add ability to retrieve action name
public class RpcUnknownCommandException extends RpcExternalException {
    
    public RpcUnknownCommandException(String rawMessage) {
        super("Unrecognized command action (is jNano updated?)", rawMessage);
    }
    
}
