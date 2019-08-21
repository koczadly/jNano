package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWalletLocked;

public class RequestWalletLock extends RpcRequest<ResponseWalletLocked> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    
    public RequestWalletLock(String walletId) {
        super("wallet_lock", ResponseWalletLocked.class);
        this.walletId = walletId;
    }
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
