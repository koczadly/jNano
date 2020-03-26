package uk.oczadly.karl.jnano.rpc;

import com.google.gson.JsonObject;

/**
 * Represents a response from a Nano RPC query.
 */
public abstract class RpcResponse {
    
    private JsonObject rawJson;
    
    
    /**
     * @return the raw JSON response data sent from the node
     */
    public final JsonObject getRawResponseJson() {
        return this.rawJson;
    }
    
    @Override
    public String toString() {
        return getRawResponseJson().toString();
    }
    
    
    /** Internal method for initializing */
    final void initResponseObject(JsonObject rawJson) {
        if (this.rawJson != null)
            throw new IllegalStateException("Response is already initialized");
        
        this.rawJson = rawJson;
    }
    
}
