package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.generic.AccountFrontiersResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class WalletFetchFrontiersRequest extends RPCRequest<AccountFrontiersResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    
    public WalletFetchFrontiersRequest(String walletId) {
        super("wallet_frontiers", AccountFrontiersResponse.class);
        this.walletId = walletId;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
