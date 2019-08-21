package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseKey;

public class RequestAccountPublicKey extends RpcRequest<ResponseKey> {
    
    @Expose @SerializedName("account")
    private String account;
    
    
    public RequestAccountPublicKey(String account) {
        super("account_key", ResponseKey.class);
        this.account = account;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
}
