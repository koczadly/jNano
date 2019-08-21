package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWalletLocked;

public class RequestWalletLocked extends RpcRequest<ResponseWalletLocked> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    
    public RequestWalletLocked(String walletId) {
        super("wallet_locked", ResponseWalletLocked.class);
        this.walletId = walletId;
    }
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
