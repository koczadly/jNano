package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.WalletLockedResponse;

public class WalletLockedRequest extends RpcRequest<WalletLockedResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    
    public WalletLockedRequest(String walletId) {
        super("wallet_locked", WalletLockedResponse.class);
        this.walletId = walletId;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
