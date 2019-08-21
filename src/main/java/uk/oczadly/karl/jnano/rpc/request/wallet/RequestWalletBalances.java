package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseMultiAccountBalances;

import java.math.BigInteger;

public class RequestWalletBalances extends RpcRequest<ResponseMultiAccountBalances> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    @Expose @SerializedName("threshold")
    private BigInteger threshold;
    
    
    public RequestWalletBalances(String walletId) {
        this(walletId, null);
    }
    
    public RequestWalletBalances(String walletId, BigInteger threshold) {
        super("wallet_balances", ResponseMultiAccountBalances.class);
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
