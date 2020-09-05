/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.exception;

import com.google.gson.JsonParseException;

/**
 * Thrown if the node returns an invalid JSON response.
 *
 * <p>This exception being thrown could indicate a possible error with the configured deserializer(s) used by the
 * response class.</p>
 *
 * <p>If this exception occurs with a built-in set of request/response classes,
 * <em>please</em> <a href="https://github.com/koczadly/jNano/issues">create an issue on GitHub.</a></p>
 */
public class RpcInvalidResponseException extends RpcException {
    
    private final String response;
    
    public RpcInvalidResponseException(String response, JsonParseException source) {
        super("Unable to parse/deserialize the received JSON response.", source);
        this.response = response;
    }
    
    
    public String getResponseBody() {
        return response;
    }
    
    @Override
    public synchronized JsonParseException getCause() {
        return (JsonParseException)super.getCause();
    }
    
}
