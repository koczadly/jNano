package uk.oczadly.karl.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.AccountResponse;

public class AccountRepresentativeRequest extends RpcRequest<AccountResponse> {
    
    @Expose
    @SerializedName("account")
    private String account;
    
    
    public AccountRepresentativeRequest(String account) {
        super("account_representative", AccountResponse.class);
        this.account = account;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
}
