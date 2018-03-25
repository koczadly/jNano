package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.generic.BlockHashesResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class WalletRepublishBlocksRequest extends RPCRequest<BlockHashesResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    @Expose
    @SerializedName("count")
    private int count;
    
    
    public WalletRepublishBlocksRequest(String walletId, int count) {
        super("wallet_republish", BlockHashesResponse.class);
        this.walletId = walletId;
        this.count = count;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
    public int getCount() {
        return count;
    }
    
}
