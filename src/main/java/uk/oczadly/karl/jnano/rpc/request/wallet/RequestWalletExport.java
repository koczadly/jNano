package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWalletExport;

public class RequestWalletExport extends RpcRequest<ResponseWalletExport> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    
    public RequestWalletExport(String walletId) {
        super("wallet_export", ResponseWalletExport.class);
        this.walletId = walletId;
    }
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
