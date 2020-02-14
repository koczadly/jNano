package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccountHistory;

/**
 * This request class is used to request an account's transaction history.
 * The server responds with a {@link ResponseAccountHistory} data object.<br>
 * Calls the internal RPC method {@code account_history}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#account_history">Official RPC documentation</a>
 */
public class RequestAccountHistory extends RpcRequest<ResponseAccountHistory> {
    
    @Expose @SerializedName("raw")
    private final boolean raw = true;
    
    
    @Expose @SerializedName("account")
    private final String account;
    
    @Expose @SerializedName("count")
    private final int transactionCount;
    
    
    @Expose @SerializedName("head")
    private final String head;
    
    @Expose @SerializedName("offset")
    private final Integer offset;
    
    @Expose @SerializedName("reverse")
    private final Boolean reverse;
    
    @Expose @SerializedName("account_filter")
    private final String[] accountFilter;
    
    
    /**
     * @param account   the account's address
     */
    public RequestAccountHistory(String account) {
        this(account, null);
    }
    
    /**
     * @param account           the account's address
     * @param transactionCount  (optional) the maximum number of transactions to fetch
     */
    public RequestAccountHistory(String account, Integer transactionCount) {
        this(account, transactionCount, null, null, null, null);
    }
    
    /**
     * @param account           the account's address
     * @param transactionCount  (optional) the maximum number of transactions to fetch
     * @param head              (optional) the head block hash
     * @param offset            (optional) how many blocks to skip after the head
     * @param reverse           (optional) whether the list should list backwards from the head
     * @param accountFilter     (optional) a list of accounts to filter by
     */
    public RequestAccountHistory(String account, Integer transactionCount, String head, Integer offset, Boolean reverse, String[] accountFilter) {
        super("account_history", ResponseAccountHistory.class);
        this.account = account;
        this.transactionCount = transactionCount != null ? transactionCount : -1;
        this.head = head;
        this.offset = offset;
        this.reverse = reverse;
        this.accountFilter = accountFilter;
    }
    
    
    /**
     * @return the requested account's address
     */
    public String getAccount() {
        return account;
    }
    
    /**
     * @return the requested transaction limit
     */
    public int getTransactionCount() {
        return transactionCount;
    }
    
    /**
     * @return the requested head block's hash
     */
    public String getHead() {
        return head;
    }
    
    /**
     * @return the requested offset
     */
    public Integer getOffset() {
        return offset;
    }
    
    /**
     * @return the requested ordering
     */
    public Boolean getReverse() {
        return reverse;
    }
    
    /**
     * @return the requested account filters
     */
    public String[] getAccountFilters() {
        return accountFilter;
    }
    
}
