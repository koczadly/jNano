package uk.oczadly.karl.jnano.rpc.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.AccountFrontiersResponse;

public class AccountsFrontiersRequest extends RpcRequest<AccountFrontiersResponse> {
    
    @Expose
    @SerializedName("accounts")
    private String[] accounts;
    
    
    public AccountsFrontiersRequest(String... accounts) {
        super("accounts_frontiers", AccountFrontiersResponse.class);
        this.accounts = accounts;
    }
    
    
    
    public String[] getAccounts() {
        return accounts;
    }
    
}
