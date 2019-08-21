package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWalletHistory;

public class RequestWalletHistory extends RpcRequest<ResponseWalletHistory> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    @Expose @SerializedName("modified_since")
    private Integer modifiedSince;
    
    
    public RequestWalletHistory(String walletId) {
        this(walletId, null);
    }
    
    public RequestWalletHistory(String walletId, Integer modifiedSince) {
        super("wallet_history", ResponseWalletHistory.class);
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
