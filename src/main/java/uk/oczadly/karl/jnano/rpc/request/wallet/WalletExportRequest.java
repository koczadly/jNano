package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.WalletExportResponse;

public class WalletExportRequest extends RpcRequest<WalletExportResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    
    public WalletExportRequest(String walletId) {
        super("wallet_export", WalletExportResponse.class);
        this.walletId = walletId;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
