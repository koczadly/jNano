package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.AccountsResponse;

public class AccountListRequest extends RpcRequest<AccountsResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    
    public AccountListRequest(String walletId) {
        super("account_list", AccountsResponse.class);
        this.walletId = walletId;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
