package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This response class contains a boolean which represents whether something exists.
 */
public class ResponseExists extends RpcResponse {
    
    @Expose @SerializedName("exists")
    private boolean exists;
    
    
    /**
     * @return if the entity exists
     */
    public boolean doesExist() {
        return exists;
    }
    
}
