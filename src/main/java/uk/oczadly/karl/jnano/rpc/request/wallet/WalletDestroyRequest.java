package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;

public class WalletDestroyRequest extends RpcRequest<RpcResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    
    public WalletDestroyRequest(String walletId) {
        super("wallet_destroy", RpcResponse.class);
        this.walletId = walletId;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
