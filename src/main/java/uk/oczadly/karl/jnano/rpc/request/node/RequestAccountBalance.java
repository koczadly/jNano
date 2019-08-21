package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBalance;

/**
 * This request class is used to retrieve the balance
 */
public class RequestAccountBalance extends RpcRequest<ResponseBalance> {
    
    @Expose @SerializedName("account")
    private String account;
    
    
    public RequestAccountBalance(String account) {
        super("account_balance", ResponseBalance.class);
        this.account = account;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
}
