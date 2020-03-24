package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWalletHistory;

/**
 * This request class is used to fetch transaction history for all accounts within a wallet.
 * <br>Calls the RPC command {@code wallet_history}, and returns a {@link ResponseWalletHistory} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#wallet_history">Official RPC documentation</a>
 */
public class RequestWalletHistory extends RpcRequest<ResponseWalletHistory> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    @Expose @SerializedName("modified_since")
    private final Integer modifiedSince;
    
    
    /**
     * @param walletId the wallet's ID
     */
    public RequestWalletHistory(String walletId) {
        this(walletId, null);
    }
    
    /**
     * @param walletId      the wallet's ID
     * @param modifiedSince filter out transactions before the specified UNIX timestamp
     */
    public RequestWalletHistory(String walletId, Integer modifiedSince) {
        super("wallet_history", ResponseWalletHistory.class);
        this.walletId = walletId;
        this.modifiedSince = modifiedSince;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
    /**
     * @return the modified since threshold
     */
    public Integer getModifiedSince() {
        return modifiedSince;
    }
    
}
