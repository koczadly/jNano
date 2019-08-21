package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseMultiAccountBalances;

public class RequestMultiAccountBalances extends RpcRequest<ResponseMultiAccountBalances> {
    
    @Expose
    @SerializedName("accounts")
    private String[] accounts;
    
    
    public RequestMultiAccountBalances(String... accounts) {
        super("accounts_balances", ResponseMultiAccountBalances.class);
        this.accounts = accounts;
    }
    
    
    
    public String[] getAccounts() {
        return accounts;
    }
    
}
