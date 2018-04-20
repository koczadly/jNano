package uk.oczadly.karl.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.WalletPendingBlocksResponse;

import java.math.BigInteger;

public class WalletPendingBlocksRequest extends RpcRequest<WalletPendingBlocksResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    @Expose
    @SerializedName("count")
    private int count;
    
    @Expose
    @SerializedName("threshold")
    private BigInteger threshold;
    
    @Expose
    @SerializedName("source")
    private boolean source = true;
    
    
    public WalletPendingBlocksRequest(String walletId, int count) {
        this(walletId, count, BigInteger.ZERO);
    }
    
    public WalletPendingBlocksRequest(String walletId, int count, BigInteger threshold) {
        super("wallet_pending", WalletPendingBlocksResponse.class);
        this.walletId = walletId;
        this.count = count;
        this.threshold = threshold;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
    public int getCount() {
        return count;
    }
    
    public BigInteger getThreshold() {
        return threshold;
    }
    
}
