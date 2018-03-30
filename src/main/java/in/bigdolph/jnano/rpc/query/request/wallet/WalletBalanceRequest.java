package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.BalanceResponse;

public class WalletBalanceRequest extends RPCRequest<BalanceResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    
    public WalletBalanceRequest(String walletId) {
        super("wallet_balance_total", BalanceResponse.class);
        this.walletId = walletId;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
