package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseCount extends RpcResponse {

    @Expose @SerializedName("count")
    private long count;
    
    
    public long getCount() {
        return count;
    }
    
}
