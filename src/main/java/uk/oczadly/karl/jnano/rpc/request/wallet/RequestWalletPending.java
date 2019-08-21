package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWalletPending;

import java.math.BigInteger;

public class RequestWalletPending extends RpcRequest<ResponseWalletPending> {
    
    @Expose @SerializedName("source")
    private final boolean source = true;
    
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    @Expose @SerializedName("count")
    private int count;
    
    
    @Expose @SerializedName("threshold")
    private BigInteger threshold;
    
    @Expose @SerializedName("include_active")
    private Boolean includeActive;
    
    @Expose @SerializedName("include_only_confirmed")
    private Boolean includeOnlyConfirmed;
    
    
    public RequestWalletPending(String walletId, int count) {
        this(walletId, count, null, null, null);
    }
    
    public RequestWalletPending(String walletId, int count, BigInteger threshold, Boolean includeActive,
                                Boolean includeOnlyConfirmed) {
        super("wallet_pending", ResponseWalletPending.class);
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
    
    public Boolean getIncludeActive() {
        return includeActive;
    }
    
    public Boolean getIncludeOnlyConfirmed() {
        return includeOnlyConfirmed;
    }
    
}
