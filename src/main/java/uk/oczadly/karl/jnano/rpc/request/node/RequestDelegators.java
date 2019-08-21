package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseDelegators;

public class RequestDelegators extends RpcRequest<ResponseDelegators> {
    
    @Expose @SerializedName("account")
    private String account;
    
    
    public RequestDelegators(String account) {
        super("delegators", ResponseDelegators.class);
        this.account = account;
    }
    
    
    public String getAccount() {
        return account;
    }
    
}
