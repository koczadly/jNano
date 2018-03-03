package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.specific.AccountWeightResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class AccountWeightRequest extends RPCRequest<AccountWeightResponse> {
    
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
