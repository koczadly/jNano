package uk.oczadly.karl.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ValidationResponse extends RpcResponse {

    @Expose
    @SerializedName("valid")
    private boolean isValid;
    
    
    public boolean isValid() {
        return isValid;
    }
    
}
