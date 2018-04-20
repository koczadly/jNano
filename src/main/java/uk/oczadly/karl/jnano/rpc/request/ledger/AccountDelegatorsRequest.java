package uk.oczadly.karl.jnano.rpc.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.AccountDelegatorsResponse;

public class AccountDelegatorsRequest extends RpcRequest<AccountDelegatorsResponse> {
    
    @Expose
    @SerializedName("account")
    private String account;
    
    
    public AccountDelegatorsRequest(String account) {
        super("delegators", AccountDelegatorsResponse.class);
        this.account = account;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
}
