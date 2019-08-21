package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWalletChangeSeed;

public class RequestWalletChangeSeed extends RpcRequest<ResponseWalletChangeSeed> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    @Expose @SerializedName("seed")
    private String seed;
    
    @Expose @SerializedName("count")
    private Integer count;
    
    
    public RequestWalletChangeSeed(String walletId, String seed) {
        this(walletId, seed, null);
    }
    
    public RequestWalletChangeSeed(String walletId, String seed, Integer count) {
        super("wallet_change_seed", ResponseWalletChangeSeed.class);
        this.walletId = walletId;
        this.seed = seed;
        this.count = count;
    }
    
    
    public String getWalletId() {
        return walletId;
    }
    
    public String getSeed() {
        return seed;
    }
    
    public Integer getCount() {
        return count;
    }
    
}
