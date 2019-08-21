package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseMultiAccountFrontiers;

public class RequestWalletFrontiers extends RpcRequest<ResponseMultiAccountFrontiers> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    
    public RequestWalletFrontiers(String walletId) {
        super("wallet_frontiers", ResponseMultiAccountFrontiers.class);
        this.walletId = walletId;
    }
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
