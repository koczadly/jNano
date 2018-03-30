package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.AccountDelegatorsResponse;

public class AccountDelegatorsRequest extends RpcRequest<AccountDelegatorsResponse> {
    
    @Expose
    @SerializedName("account")
    private String account;
    
    
    public AccountDelegatorsRequest(String account) {
        super("delegators", AccountDelegatorsResponse.class);
        this.account = account;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
}
