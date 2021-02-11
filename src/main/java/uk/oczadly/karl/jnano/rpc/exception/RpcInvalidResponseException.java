/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.exception;

/**
 * Thrown if the node returns an invalid (JSON) response.
 *
 * <p>This exception being thrown could indicate a possible error with the configured deserializer(s) used by the
 * response class.</p>
 *
 * <p>If this exception occurs with a built-in set of request/response classes,
 * <em>please</em> <a href="https://github.com/koczadly/jNano/issues">create an issue on GitHub.</a></p>
 */
public class RpcInvalidResponseException extends RpcException {
    
    private final String response;
    
    public RpcInvalidResponseException(String message, String response) {
        super(message);
        this.response = response;
    }
    
    public RpcInvalidResponseException(String response, Throwable cause) {
        super("Unable to parse/deserialize the received JSON response.", cause);
        this.response = response;
    }
    
    
    /**
     * @return the raw response data
     */
    public String getResponseBody() {
        return response;
    }
    
}
