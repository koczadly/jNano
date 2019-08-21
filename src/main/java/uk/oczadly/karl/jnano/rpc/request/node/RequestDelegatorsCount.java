package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseCount;

public class RequestDelegatorsCount extends RpcRequest<ResponseCount> {
    
    @Expose
    @SerializedName("account")
    private String account;
    
    
    public RequestDelegatorsCount(String account) {
        super("delegators_count", ResponseCount.class);
        this.account = account;
    }
    
    
    public String getAccount() {
        return account;
    }
    
}
