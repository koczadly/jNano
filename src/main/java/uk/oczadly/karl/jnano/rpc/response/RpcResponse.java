package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.JsonObject;

public class RpcResponse {
    
    private JsonObject rawJson;
    
    protected RpcResponse() {}
    
    
    public final JsonObject getRawJSON() {
        return this.rawJson;
    }
    
    /** This method should only be called after instantiation */
    public final void init(JsonObject rawJson) {
        if(this.rawJson != null) throw new IllegalStateException("Initial parameters have already been set");
        this.rawJson = rawJson;
    }
    
}
