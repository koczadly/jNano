package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAmount;

public class RequestAccountWeight extends RpcRequest<ResponseAmount> {
    
    @Expose
    @SerializedName("account")
    private String account;
    
    
    public RequestAccountWeight(String account) {
        super("account_weight", ResponseAmount.class);
        this.account = account;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
}
