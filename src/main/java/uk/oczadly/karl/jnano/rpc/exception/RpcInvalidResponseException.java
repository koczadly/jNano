package uk.oczadly.karl.jnano.rpc.exception;

import com.google.gson.JsonParseException;

/**
 * Thrown if the node returns an invalid JSON response.
 */
public class RpcInvalidResponseException extends RpcException {
    
    private final String response;
    
    public RpcInvalidResponseException(String response, JsonParseException source) {
        super("Unable to parse JSON response.", source);
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
