package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.specific.AccountsFrontiersResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class AccountsFrontiersRequest extends RPCRequest<AccountsFrontiersResponse> {
    
    @Expose
    @SerializedName("accounts")
    private String[] accounts;
    
    
    public AccountsFrontiersRequest(String... accounts) {
        super("accounts_frontiers", AccountsFrontiersResponse.class);
        this.accounts = accounts;
    }
    
    
    
    public String[] getAccounts() {
        return accounts;
    }
    
}
