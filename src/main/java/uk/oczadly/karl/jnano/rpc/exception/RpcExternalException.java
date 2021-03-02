/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.exception;

/**
 * This exception is thrown when an RPC error is externally generated and returned by the node.
 */
public abstract class RpcExternalException extends RpcException {
    
    private final String rawMessage;
    
    /**
     * @param rawMessage the raw error message received from the node
     */
    public RpcExternalException(String rawMessage) {
        this(formatMessage(rawMessage), rawMessage);
    }
    
    /**
     * @param message    the friendly exception message
     * @param rawMessage the raw error message received from the node
     */
    public RpcExternalException(String message, String rawMessage) {
        super(message);
        this.rawMessage = rawMessage;
    }
    
    
    /**
     * Returns the error string generated and returned by the node.
     * @return the error message from the node
     */
    public final String getRawMessage() {
        return rawMessage;
    }
    
    
    protected static String formatMessage(String msg) {
        if (msg == null || msg.isEmpty())
            return null;
        StringBuilder sb = new StringBuilder(msg.length() + 1);
        sb.append(Character.toUpperCase(msg.charAt(0)));
        sb.append(msg, 1, msg.length());
        if (msg.charAt(msg.length() - 1) != '.')
            sb.append('.');
        return sb.toString();
    }
    
}
