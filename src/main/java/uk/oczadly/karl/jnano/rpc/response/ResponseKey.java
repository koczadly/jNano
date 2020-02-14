package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcResponse;

/**
 * This response class contains a single String representing a key, either public or private.
 */
public class ResponseKey extends RpcResponse {

    @Expose @SerializedName("key")
    private String key;
    
    
    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }
    
}
