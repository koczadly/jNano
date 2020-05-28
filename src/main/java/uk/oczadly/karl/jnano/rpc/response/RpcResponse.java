package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Represents a response from a Nano RPC query.
 * <p>This class represents an RPC response, containing all of the structured data returned by the node.</p>
 * <p>Classes which extend this class need to specify parameters as private fields, and MUST be marked with Gson's
 * {@link Expose} annotation. The parameter name will be derived from the name of the field, unless specified otherwise
 * using the {@link SerializedName} annotation.</p>
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
    
}
