package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWalletInfo;

public class RequestWalletInfo extends RpcRequest<ResponseWalletInfo> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    
    public RequestWalletInfo(String walletId) {
        super("wallet_frontiers", ResponseWalletInfo.class);
        this.walletId = walletId;
    }
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
