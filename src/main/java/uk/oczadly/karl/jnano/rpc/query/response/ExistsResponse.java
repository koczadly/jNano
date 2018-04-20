package uk.oczadly.karl.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExistsResponse extends RpcResponse {

    @Expose
    @SerializedName("exists")
    private boolean exists;
    
    
    public boolean doesExist() {
        return exists;
    }
    
}
