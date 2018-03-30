package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.WalletLockedResponse;

public class WalletLockRequest extends RPCRequest<WalletLockedResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    
    public WalletLockRequest(String walletId) {
        super("wallet_lock", WalletLockedResponse.class);
        this.walletId = walletId;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
