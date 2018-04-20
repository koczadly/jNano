package uk.oczadly.karl.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.BlockHashesResponse;

public class WalletRepublishBlocksRequest extends RpcRequest<BlockHashesResponse> {
    
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
