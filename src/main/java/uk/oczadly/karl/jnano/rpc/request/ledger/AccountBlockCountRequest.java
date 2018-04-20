package uk.oczadly.karl.jnano.rpc.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.AccountBlockCountResponse;

public class AccountBlockCountRequest extends RpcRequest<AccountBlockCountResponse> {
    
    @Expose
    @SerializedName("account")
    private String account;
    
    
    public AccountBlockCountRequest(String account) {
        super("account_block_count", AccountBlockCountResponse.class);
        this.account = account;
    }
    
    
    
    public String getAccount() {
        return account;
    }
}
