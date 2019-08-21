package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseAccount extends RpcResponse {

    @Expose @SerializedName(value = "account", alternate = {"representative"})
    private String address;
    
    
    public String getAccountAddress() {
        return address;
    }
    
}
