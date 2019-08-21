package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccount;

public class RequestAccountRepresentative extends RpcRequest<ResponseAccount> {
    
    @Expose
    @SerializedName("account")
    private String account;
    
    
    public RequestAccountRepresentative(String account) {
        super("account_representative", ResponseAccount.class);
        this.account = account;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
}
