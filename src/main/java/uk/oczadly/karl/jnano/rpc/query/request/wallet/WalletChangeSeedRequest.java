package uk.oczadly.karl.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.RpcResponse;

public class WalletChangeSeedRequest extends RpcRequest<RpcResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    @Expose
    @SerializedName("seed")
    private String seed;
    
    
    public WalletChangeSeedRequest(String walletId, String seed) {
        super("wallet_change_seed", RpcResponse.class);
        this.walletId = walletId;
        this.seed = seed;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
    public String getSeed() {
        return seed;
    }
    
}
