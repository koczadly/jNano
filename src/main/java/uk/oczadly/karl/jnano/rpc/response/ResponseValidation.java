package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcResponse;

/**
 * This response class contains a single boolean which represents if something is valid.
 */
public class ResponseValidation extends RpcResponse {
    
    @Expose @SerializedName("valid")
    private boolean isValid;
    
    
    /**
     * @return whether the request was valid
     */
    public boolean isValid() {
        return isValid;
    }
    
}
