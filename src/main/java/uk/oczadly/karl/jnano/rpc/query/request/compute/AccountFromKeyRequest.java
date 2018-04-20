package uk.oczadly.karl.jnano.rpc.query.request.compute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.AccountResponse;

public class AccountFromKeyRequest extends RpcRequest<AccountResponse> {
    
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
