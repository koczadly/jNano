package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Set;

/**
 * This response class contains a set of account addresses.
 */
public class ResponseAccounts extends RpcResponse {
    
    @Expose @SerializedName("accounts")
    private Set<String> accounts;
    
    
    /**
     * @return a set of account addresses
     */
    public Set<String> getAccounts() {
        return accounts;
    }
    
}
