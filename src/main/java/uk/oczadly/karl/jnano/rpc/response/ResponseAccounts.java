package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Set;

public class ResponseAccounts extends RpcResponse {

    @Expose @SerializedName("accounts")
    private Set<String> accounts;
    
    
    public Set<String> getAccounts() {
        return accounts;
    }
    
}
