package in.bigdolph.jnano.rpc.query.response.generic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

public class WalletResponse extends RPCResponse {

    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
