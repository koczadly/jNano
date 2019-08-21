package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

public class RequestWalletDestroy extends RpcRequest<ResponseSuccessful> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    
    public RequestWalletDestroy(String walletId) {
        super("wallet_destroy", ResponseSuccessful.class);
        this.walletId = walletId;
    }
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
