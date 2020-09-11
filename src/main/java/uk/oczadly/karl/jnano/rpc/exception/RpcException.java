/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.exception;

/**
 * The default base RPC exception. This is thrown when there is an error with the request or node.
 */
public class RpcException extends Exception {
    
    private final String nodeMessage;
    
    
    public RpcException(String message) {
        this(message, (String)null);
    }
    
    public RpcException(String message, Throwable cause) {
        this(message, null, cause);
    }
    
    public RpcException(String message, String nodeMessage) {
        this(message, nodeMessage, null);
    }
    
    public RpcException(String message, String nodeMessage, Throwable cause) {
        super(message == null && nodeMessage != null ? formatMessage(nodeMessage) : message, cause);
        this.nodeMessage = nodeMessage;
    }
    
    
    /**
     * Returns the exact error string returned by the node.
     * @return the error message from the node, or null
     */
    public final String getNodeMessage() {
        return nodeMessage;
    }
    
    
    protected static String formatMessage(String msg) {
        if (msg == null || msg.isEmpty())
            return null;
        
        StringBuilder sb = new StringBuilder(msg.length() + 1);
        sb.append(Character.toUpperCase(msg.charAt(0)));
        sb.append(msg, 1, msg.length());
        sb.append('.');
        return sb.toString();
    }
    
}
