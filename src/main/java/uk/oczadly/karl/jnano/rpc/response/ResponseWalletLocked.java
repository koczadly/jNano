package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseWalletLocked extends RpcResponse {

    @Expose @SerializedName("locked")
    private boolean isLocked;
    
    
    public boolean isWalletLocked() {
        return isLocked;
    }
    
}
