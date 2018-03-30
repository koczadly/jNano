package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.CountResponse;

public class AccountDelegatorsCountRequest extends RpcRequest<CountResponse> {
    
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
