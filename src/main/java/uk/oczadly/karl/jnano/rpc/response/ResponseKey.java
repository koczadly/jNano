package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseKey extends RpcResponse {

    @Expose @SerializedName("key")
    private String key;
    
    
    public String getKey() {
        return key;
    }
    
}
