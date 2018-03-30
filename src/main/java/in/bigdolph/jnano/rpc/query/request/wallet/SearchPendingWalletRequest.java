package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

public class SearchPendingWalletRequest extends RPCRequest<RPCResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    
    public SearchPendingWalletRequest(String walletId) {
        super("search_pending", RPCResponse.class);
        this.walletId = walletId;
    }
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
