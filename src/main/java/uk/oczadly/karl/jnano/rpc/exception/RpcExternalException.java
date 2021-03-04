/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.exception;

import uk.oczadly.karl.jnano.rpc.JsonResponseDeserializer;
import uk.oczadly.karl.jnano.rpc.RpcQueryNode;
import uk.oczadly.karl.jnano.rpc.RpcResponseDeserializer;

/**
 * This exception is thrown when an RPC error is externally generated and returned by the node.
 *
 * <p>Exceptions extending this base class should always pass the raw message argument directly from the
 * deserializer. Custom exception parsing may be added to an {@link RpcQueryNode} instance by specifying your own
 * {@link RpcResponseDeserializer} implementation, or by overriding the {@code parseException} method in the standard
 * {@link JsonResponseDeserializer}.</p>
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
    
    
    private static String formatMessage(String rawMessage) {
        if (rawMessage == null || rawMessage.isEmpty())
            return null;
        StringBuilder sb = new StringBuilder(rawMessage.length() + 1);
        sb.append(Character.toUpperCase(rawMessage.charAt(0)));
        sb.append(rawMessage, 1, rawMessage.length());
        if (rawMessage.charAt(rawMessage.length() - 1) != '.')
            sb.append('.');
        return sb.toString();
    }
    
}
