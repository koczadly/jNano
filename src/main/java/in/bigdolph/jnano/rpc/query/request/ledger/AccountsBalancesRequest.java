package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.specific.AccountsBalanceResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class AccountsBalancesRequest extends RPCRequest<AccountsBalanceResponse> {
    
    @Expose
    @SerializedName("accounts")
    private String[] accounts;
    
    
    public AccountsBalancesRequest(String... accounts) {
        super("accounts_balances", AccountsBalanceResponse.class);
        this.accounts = accounts;
    }
    
    
    
    public String[] getAccounts() {
        return accounts;
    }
    
}
