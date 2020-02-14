package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWalletPending;

import java.math.BigInteger;

/**
 * This request class is used to fetch a list of pending blocks which have not been received by accounts in the
 * specified wallet.
 * The server responds with a {@link ResponseWalletPending} data object.<br>
 * Calls the internal RPC method {@code wallet_pending}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#wallet_pending">Official RPC documentation</a>
 */
public class RequestWalletPending extends RpcRequest<ResponseWalletPending> {
    
    @Expose @SerializedName("source")
    private final boolean source = true;
    
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    @Expose @SerializedName("count")
    private final int count;
    
    
    @Expose @SerializedName("threshold")
    private final BigInteger threshold;
    
    @Expose @SerializedName("include_active")
    private final Boolean includeActive;
    
    @Expose @SerializedName("include_only_confirmed")
    private final Boolean includeOnlyConfirmed;
    
    
    /**
     * @param walletId  the wallet's ID
     * @param count     the number of blocks to limit
     */
    public RequestWalletPending(String walletId, int count) {
        this(walletId, count, null, null, null);
    }
    
    /**
     * @param walletId              the wallet's ID
     * @param count                 the number of blocks to limit
     * @param threshold             (optional) the minimum threshold amount in RAW
     * @param includeActive         (optional) whether active blocks should be included
     * @param includeOnlyConfirmed  (optional) whether only blocks undergoing height processing should be listed
     */
    public RequestWalletPending(String walletId, int count, BigInteger threshold, Boolean includeActive,
                                Boolean includeOnlyConfirmed) {
        super("wallet_pending", ResponseWalletPending.class);
        this.walletId = walletId;
        this.count = count;
        this.threshold = threshold;
        this.includeActive = includeActive;
        this.includeOnlyConfirmed = includeOnlyConfirmed;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
    /**
     * @return the requested block limit
     */
    public int getCount() {
        return count;
    }
    
    /**
     * @return the requested threshold amount
     */
    public BigInteger getThreshold() {
        return threshold;
    }
    
    /**
     * @return whether active blocks should be included
     */
    public Boolean getIncludeActive() {
        return includeActive;
    }
    
    /**
     * @return whether only blocks with a confirmation height should be listed
     */
    public Boolean getIncludeOnlyConfirmed() {
        return includeOnlyConfirmed;
    }
    
}
