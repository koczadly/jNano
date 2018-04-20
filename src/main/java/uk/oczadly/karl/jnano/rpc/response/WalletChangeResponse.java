package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletChangeResponse extends RpcResponse {
    
    @Expose
    @SerializedName("changed")
    private boolean hasChanged;
    
    
    public boolean hasBeenChanged() {
        return hasChanged;
    }
    
}
