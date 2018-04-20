package uk.oczadly.karl.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountGetKeyResponse extends RpcResponse {

    @Expose
    @SerializedName("key")
    private String key;
    
    
    
    public String getPublicKey() {
        return key;
    }
    
}
