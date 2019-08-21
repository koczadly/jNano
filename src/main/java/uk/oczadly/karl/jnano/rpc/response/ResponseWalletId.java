package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseWalletId extends RpcResponse {

    @Expose @SerializedName("wallet")
    private String walletId;
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
