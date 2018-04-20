package uk.oczadly.karl.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.BalanceResponse;

public class WalletBalanceRequest extends RpcRequest<BalanceResponse> {
    
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
