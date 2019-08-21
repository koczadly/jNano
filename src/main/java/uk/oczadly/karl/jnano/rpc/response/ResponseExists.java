package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseExists extends RpcResponse {

    @Expose @SerializedName("exists")
    private boolean exists;
    
    
    public boolean doesExist() {
        return exists;
    }
    
}
