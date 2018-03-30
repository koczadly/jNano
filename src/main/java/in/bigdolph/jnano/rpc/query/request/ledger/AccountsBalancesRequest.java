package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.BalancesResponse;

public class AccountsBalancesRequest extends RpcRequest<BalancesResponse> {
    
    @Expose
    @SerializedName("accounts")
    private String[] accounts;
    
    
    public AccountsBalancesRequest(String... accounts) {
        super("accounts_balances", BalancesResponse.class);
        this.accounts = accounts;
    }
    
    
    
    public String[] getAccounts() {
        return accounts;
    }
    
}
