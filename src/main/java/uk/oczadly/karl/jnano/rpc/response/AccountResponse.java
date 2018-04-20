package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountResponse extends RpcResponse {

    @Expose
    @SerializedName("account")
    private String accountAddress;
    
    
    
    public String getAccountAddress() {
        return accountAddress;
    }
    
}
