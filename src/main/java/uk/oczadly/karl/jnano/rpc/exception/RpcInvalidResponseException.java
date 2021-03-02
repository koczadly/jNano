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
    
    private final String responseBody;
    
    public RpcInvalidResponseException(String message, String responseBody) {
        super(message);
        this.responseBody = responseBody;
    }
    
    public RpcInvalidResponseException(Throwable cause, String responseBody) {
        super("Unable to parse the response as JSON.", cause);
        this.responseBody = responseBody;
    }
    
    
    /**
     * @return the raw response data received from the node
     */
    public String getResponseBody() {
        return responseBody;
    }
    
}
