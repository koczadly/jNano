package in.bigdolph.jnano.rpc.query.request.compute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.AccountResponse;

public class AccountFromKeyRequest extends RPCRequest<AccountResponse> {
    
    @Expose
    @SerializedName("key")
    private String accountKey;
    
    
    public AccountFromKeyRequest(String accountKey) {
        super("account_get", AccountResponse.class);
        this.accountKey = accountKey;
    }
    
    
    
    public String getAccountKey() {
        return accountKey;
    }
}
