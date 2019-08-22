package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.JsonObject;

/**
 * Represents a response from a Nano RPC query.
 */
public abstract class RpcResponse {
    
    private JsonObject rawJson;
    
    protected RpcResponse() {}
    
    
    /**
     * @return the raw JSON data sent from the node
     */
    public final JsonObject getRawJSON() {
        return this.rawJson;
    }
    
    /** This method should only be called after instantiation, and is done automatically through the RPC processor. */
    public synchronized final void init(JsonObject rawJson) {
        //Double-checked locking to prevent multi-threading issues
        if(this.rawJson != null)
            throw new IllegalStateException("Initial parameters have already been set");
        this.rawJson = rawJson;
    }
    
}
