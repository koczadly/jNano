package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletLockedResponse extends RpcResponse {

    @Expose
    @SerializedName("locked")
    private boolean isLocked;
    
    
    public boolean isWalletLocked() {
        return isLocked;
    }
    
}
