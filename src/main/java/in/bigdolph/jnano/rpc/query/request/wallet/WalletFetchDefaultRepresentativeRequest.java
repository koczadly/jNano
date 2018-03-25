package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;
import in.bigdolph.jnano.rpc.query.response.specific.WalletFetchDefaultRepresentativeResponse;

public class WalletFetchDefaultRepresentativeRequest extends RPCRequest<WalletFetchDefaultRepresentativeResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    
    public WalletFetchDefaultRepresentativeRequest(String walletId) {
        super("wallet_representative", WalletFetchDefaultRepresentativeResponse.class);
        this.walletId = walletId;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
