package uk.oczadly.karl.jnano.rpc;

import com.google.gson.JsonObject;

/**
 * Represents a response from a Nano RPC query.
 */
public abstract class RpcResponse {
    
    private JsonObject rawJson;
    
    protected RpcResponse() {}
    
    
    /**
     * @return the raw JSON response data sent from the node
     */
    public final JsonObject getRawResponseJson() {
        return this.rawJson;
    }
    
    @Override
    public String toString() {
        return rawJson.toString();
    }
    
    
    /** Internal method for initializing */
    final void init(JsonObject rawJson) {
        this.rawJson = rawJson;
    }
    
}
