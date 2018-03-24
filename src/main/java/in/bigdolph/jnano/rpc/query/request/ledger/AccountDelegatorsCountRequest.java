package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.generic.CountResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class AccountDelegatorsCountRequest extends RPCRequest<CountResponse> {
    
    @Expose
    @SerializedName("account")
    private String account;
    
    
    public AccountDelegatorsCountRequest(String account) {
        super("delegators_count", CountResponse.class);
        this.account = account;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
}
