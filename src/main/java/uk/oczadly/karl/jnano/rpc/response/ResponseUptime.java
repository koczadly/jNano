package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseUptime extends RpcResponse {
    
    @Expose @SerializedName("seconds")
    private int seconds;
    
    
    public int getUptimeSeconds() {
        return seconds;
    }
    
}
