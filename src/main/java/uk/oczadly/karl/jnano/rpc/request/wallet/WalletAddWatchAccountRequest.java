package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;

public class WalletAddWatchAccountRequest extends RpcRequest<RpcResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    @Expose
    @SerializedName("accounts")
    private String[] accounts;
    
    
    
    public WalletAddWatchAccountRequest(String walletId, String... accounts) {
        super("wallet_add_watch", RpcResponse.class);
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
