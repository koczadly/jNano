package uk.oczadly.karl.jnano.rpc.request.compute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.AccountGetKeyResponse;

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
