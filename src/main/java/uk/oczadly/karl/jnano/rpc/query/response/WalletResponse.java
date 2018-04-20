package uk.oczadly.karl.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletResponse extends RpcResponse {

    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
