package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseMultiAccountBalances;

import java.math.BigInteger;

/**
 * This request class is used to retrieve the balances of every account within a wallet.
 * <br>Calls the RPC command {@code wallet_balances}, and returns a {@link ResponseMultiAccountBalances} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#wallet_balances">Official RPC documentation</a>
 */
public class RequestWalletBalances extends RpcRequest<ResponseMultiAccountBalances> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    @Expose @SerializedName("threshold")
    private final BigInteger threshold;
    
    
    /**
     * @param walletId the wallet's ID
     */
    public RequestWalletBalances(String walletId) {
        this(walletId, null);
    }
    
    /**
     * @param walletId  the wallet's ID
     * @param threshold the minimum threshold balance
     */
    public RequestWalletBalances(String walletId, BigInteger threshold) {
        super("wallet_balances", ResponseMultiAccountBalances.class);
        this.walletId = walletId;
        this.threshold = threshold;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
    /**
     * @return the minimum threshold balance
     */
    public BigInteger getThreshold() {
        return threshold;
    }
    
}
