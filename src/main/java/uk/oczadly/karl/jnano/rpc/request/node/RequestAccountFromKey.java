package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccount;

public class RequestAccountFromKey extends RpcRequest<ResponseAccount> {
    
    @Expose @SerializedName("key")
    private String accountKey;
    
    
    public RequestAccountFromKey(String accountKey) {
        super("account_get", ResponseAccount.class);
        this.accountKey = accountKey;
    }
    
    
    
    public String getAccountKey() {
        return accountKey;
    }
}
