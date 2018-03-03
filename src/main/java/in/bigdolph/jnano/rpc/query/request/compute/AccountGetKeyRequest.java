package in.bigdolph.jnano.rpc.query.request.compute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.specific.AccountGetKeyResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class AccountGetKeyRequest extends RPCRequest<AccountGetKeyResponse> {
    
    @Expose
    @SerializedName("account")
    private String account;
    
    
    public AccountGetKeyRequest(String account) {
        super("account_key", AccountGetKeyResponse.class);
        this.account = account;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
}
