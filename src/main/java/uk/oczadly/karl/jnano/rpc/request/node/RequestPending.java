package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponsePending;

import java.math.BigInteger;

/**
 * This request class is used to fetch a list of pending blocks which have not been received by the specified account.
 * The server responds with a {@link ResponsePending} data object.<br>
 * Calls the internal RPC method {@code pending}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#pending">Official RPC documentation</a>
 */
public class RequestPending extends RpcRequest<ResponsePending> {
    
    @Expose @SerializedName("source")
    private final boolean fetchSource = true;
    
    
    @Expose @SerializedName("account")
    private final String account;
    
    
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
     * @param account   the account's address
     * @param count     the number of blocks to limit
     */
    public RequestPending(String account, int count) {
        this(account, count, null, null, null, null);
    }
    
    /**
     * @param account               the account's address
     * @param count                 the number of blocks to limit
     * @param threshold             (optional) the minimum threshold amount in RAW
     * @param includeActive         (optional) whether active blocks should be included
     * @param sorting               (optional) whether the results should be sorted by their amounts in descending order
     * @param includeOnlyConfirmed  (optional) whether only blocks undergoing height processing should be listed
     */
    public RequestPending(String account, int count, BigInteger threshold, Boolean includeActive, Boolean sorting,
                          Boolean includeOnlyConfirmed) {
        super("pending", ResponsePending.class);
        this.account = account;
        this.count = count;
        this.threshold = threshold;
        this.includeActive = includeActive;
        this.sorting = sorting;
        this.includeOnlyConfirmed = includeOnlyConfirmed;
    }
    
    
    /**
     * @return the requested account
     */
    public String getAccount() {
        return account;
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
