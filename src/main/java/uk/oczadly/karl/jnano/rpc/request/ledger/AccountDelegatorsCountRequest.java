package uk.oczadly.karl.jnano.rpc.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.CountResponse;

public class AccountDelegatorsCountRequest extends RpcRequest<CountResponse> {
    
    @Expose
    @SerializedName("account")
    private String account;
    
    
    public AccountDelegatorsCountRequest(String account) {
        super("delegators_count", CountResponse.class);
        this.account = account;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
}
