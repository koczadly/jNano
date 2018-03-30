package in.bigdolph.jnano.rpc.query.request.compute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.AccountGetKeyResponse;

public class AccountGetKeyRequest extends RpcRequest<AccountGetKeyResponse> {
    
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
