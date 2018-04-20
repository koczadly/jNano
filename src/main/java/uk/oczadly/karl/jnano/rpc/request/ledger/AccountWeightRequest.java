package uk.oczadly.karl.jnano.rpc.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.AccountWeightResponse;

public class AccountWeightRequest extends RpcRequest<AccountWeightResponse> {
    
    @Expose
    @SerializedName("account")
    private String account;
    
    
    public AccountWeightRequest(String account) {
        super("account_weight", AccountWeightResponse.class);
        this.account = account;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
}
