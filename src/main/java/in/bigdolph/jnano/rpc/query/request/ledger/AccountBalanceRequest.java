package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.specific.AccountBalanceResponse;

public class AccountBalanceRequest extends RPCRequest<AccountBalanceResponse> {
    
    @Expose
    @SerializedName("account")
    private String account;
    
    
    public AccountBalanceRequest(String account) {
        super("account_balance", AccountBalanceResponse.class);
        this.account = account;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
}
