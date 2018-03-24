package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.generic.AccountFrontiersResponse;

public class AccountsFrontiersRequest extends RPCRequest<AccountFrontiersResponse> {
    
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
