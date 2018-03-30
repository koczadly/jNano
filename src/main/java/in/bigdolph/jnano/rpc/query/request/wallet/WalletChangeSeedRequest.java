package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

public class WalletChangeSeedRequest extends RPCRequest<RPCResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    @Expose
    @SerializedName("seed")
    private String seed;
    
    
    public WalletChangeSeedRequest(String walletId, String seed) {
        super("wallet_change_seed", RPCResponse.class);
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
