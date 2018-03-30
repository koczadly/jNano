package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.BalancesResponse;

import java.math.BigInteger;

public class WalletAccountBalancesRequest extends RPCRequest<BalancesResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    @Expose
    @SerializedName("threshold")
    private BigInteger threshold;
    
    
    public WalletAccountBalancesRequest(String walletId) {
        this(walletId, null);
    }
    
    public WalletAccountBalancesRequest(String walletId, BigInteger threshold) {
        super("wallet_balances", BalancesResponse.class);
        this.walletId = walletId;
        this.threshold = threshold;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
    public BigInteger getThreshold() {
        return threshold;
    }
    
}
