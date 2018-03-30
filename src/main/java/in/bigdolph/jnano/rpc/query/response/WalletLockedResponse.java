package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

public class WalletLockedResponse extends RPCResponse {

    @Expose
    @SerializedName("locked")
    private boolean isLocked;
    
    
    public boolean isWalletLocked() {
        return isLocked;
    }
    
}
