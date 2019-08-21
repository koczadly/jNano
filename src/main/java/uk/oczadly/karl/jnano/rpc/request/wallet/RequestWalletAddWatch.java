package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

public class RequestWalletAddWatch extends RpcRequest<ResponseSuccessful> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    @Expose @SerializedName("accounts")
    private String[] accounts;
    
    
    public RequestWalletAddWatch(String walletId, String... accounts) {
        super("wallet_add_watch", ResponseSuccessful.class);
        this.walletId = walletId;
        this.accounts = accounts;
    }
    
    
    public String getWalletId() {
        return walletId;
    }
    
    public String[] getAccounts() {
        return accounts;
    }
    
}
