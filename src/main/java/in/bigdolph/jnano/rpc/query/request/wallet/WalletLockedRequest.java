package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.WalletLockedResponse;

public class WalletLockedRequest extends RpcRequest<WalletLockedResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    
    public WalletLockedRequest(String walletId) {
        super("wallet_locked", WalletLockedResponse.class);
        this.walletId = walletId;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
