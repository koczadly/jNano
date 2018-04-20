package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.WalletFetchWorkResponse;

public class WalletFetchWorkRequest extends RpcRequest<WalletFetchWorkResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    
    public WalletFetchWorkRequest(String walletId) {
        super("wallet_work_get", WalletFetchWorkResponse.class);
        this.walletId = walletId;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
