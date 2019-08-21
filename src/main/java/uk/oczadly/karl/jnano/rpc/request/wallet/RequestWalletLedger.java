package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseLedger;

public class RequestWalletLedger extends RpcRequest<ResponseLedger> {
    
    @Expose @SerializedName("representative")
    private final boolean fetchRepresentative = true;
    
    @Expose @SerializedName("weight")
    private final boolean fetchWeight = true;
    
    @Expose @SerializedName("pending")
    private final boolean fetchPending = true;
    
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    
    @Expose @SerializedName("modified_since")
    private Integer modifiedSince;
    
    
    public RequestWalletLedger(String walletId) {
        this(walletId, null);
    }
    
    public RequestWalletLedger(String walletId, Integer modifiedSince) {
        super("wallet_ledger", ResponseLedger.class);
        this.walletId = walletId;
        this.modifiedSince = modifiedSince;
    }
    
    
    public String getWalletId() {
        return walletId;
    }
    
    public Integer getModifiedSince() {
        return modifiedSince;
    }
    
}
