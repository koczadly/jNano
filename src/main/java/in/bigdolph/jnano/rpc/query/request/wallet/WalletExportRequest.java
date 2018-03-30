package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.WalletExportResponse;

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
