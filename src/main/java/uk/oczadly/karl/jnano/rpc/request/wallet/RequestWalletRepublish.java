package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHashes;

public class RequestWalletRepublish extends RpcRequest<ResponseBlockHashes> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    @Expose @SerializedName("count")
    private int count;
    
    
    public RequestWalletRepublish(String walletId, int count) {
        super("wallet_republish", ResponseBlockHashes.class);
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
