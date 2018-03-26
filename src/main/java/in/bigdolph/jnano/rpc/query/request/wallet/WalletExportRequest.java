package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;
import in.bigdolph.jnano.rpc.query.response.specific.WalletExportResponse;

public class WalletExportRequest extends RPCRequest<WalletExportResponse> {
    
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