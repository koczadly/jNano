package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseMultiAccountsPending;

import java.math.BigInteger;

/**
 * This request class is used to fetch a list of pending block hashes for the specified accounts.
 * <br>Calls the RPC command {@code accounts_pending}, and returns a {@link ResponseMultiAccountsPending} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#accounts_pending">Official RPC documentation</a>
 */
public class RequestMultiAccountsPending extends RpcRequest<ResponseMultiAccountsPending> {
    
    @Expose @SerializedName("source")
    private final boolean source = true;
    
    
    @Expose @SerializedName("accounts")
    private final String[] accounts;
    
    @Expose @SerializedName("count")
    private final int count;
    
    
    @Expose @SerializedName("threshold")
    private final BigInteger threshold;
    
    @Expose @SerializedName("include_active")
    private final Boolean includeActive;
    
    @Expose @SerializedName("sorting")
    private final Boolean sorting;
    
    @Expose @SerializedName("include_only_confirmed")
    private final Boolean includeOnlyConfirmed;
    
    
    /**
     * @param accounts the accounts' addresses
     * @param count    the limit
     */
    public RequestMultiAccountsPending(String[] accounts, int count) {
        this(accounts, count, null, null, null, null);
    }
    
    /**
     * @param accounts             the accounts' addresses
     * @param count                the block limit
     * @param threshold            (optional) the minimum amount threshold
     * @param includeActive        (optional) whether active blocks should be included
     * @param sorting              (optional) whether each account's blocks should be sorted in descending order
     * @param includeOnlyConfirmed (optional) whether only blocks undergoing confirmation height processing should be
     *                             listed
     */
    public RequestMultiAccountsPending(String[] accounts, int count, BigInteger threshold, Boolean includeActive,
                                       Boolean sorting, Boolean includeOnlyConfirmed) {
        super("accounts_pending", ResponseMultiAccountsPending.class);
        this.accounts = accounts;
        this.count = count;
        this.threshold = threshold;
        this.includeActive = includeActive;
        this.sorting = sorting;
        this.includeOnlyConfirmed = includeOnlyConfirmed;
    }
    
    
    /**
     * @return the requested accounts
     */
    public String[] getAccounts() {
        return accounts;
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
     * @return whether the results should be in sorted order
     */
    public Boolean getSorting() {
        return sorting;
    }
    
    /**
     * @return whether only blocks with a confirmation height should be listed
     */
    public Boolean getIncludeOnlyConfirmed() {
        return includeOnlyConfirmed;
    }
    
}
