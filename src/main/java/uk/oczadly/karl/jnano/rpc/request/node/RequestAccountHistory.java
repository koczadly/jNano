/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccountHistory;

/**
 * This request class is used to request an account's transaction history.
 * <br>Calls the RPC command {@code account_history}, and returns a {@link ResponseAccountHistory} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#account_history">Official RPC documentation</a>
 */
public class RequestAccountHistory extends RpcRequest<ResponseAccountHistory> {
    
    @Expose private final String account, head;
    @Expose private final int count;
    @Expose private final Integer offset;
    @Expose private final Boolean reverse, raw;
    @Expose private final String[] accountFilter;
    
    
    /**
     * @param account the account's address
     *
     * @see #nextPage(RequestAccountHistory, ResponseAccountHistory)
     */
    public RequestAccountHistory(String account) {
        this(account, null);
    }
    
    /**
     * @param account          the account's address
     * @param count (optional) the maximum number of transactions to fetch
     *
     * @see #nextPage(RequestAccountHistory, ResponseAccountHistory)
     */
    public RequestAccountHistory(String account, Integer count) {
        this(account, count, null, null, null, null);
    }
    
    /**
     * @param account the account's address
     * @param count   (optional) the maximum number of transactions to fetch
     * @param raw     if true, all block types will be returned, and the block contents will be included
     *
     * @see #nextPage(RequestAccountHistory, ResponseAccountHistory)
     */
    public RequestAccountHistory(String account, Integer count, boolean raw) {
        this(account, count, null, null, raw, null, null);
    }
    
    /**
     * @param account          the account's address
     * @param count (optional) the maximum number of transactions to fetch
     * @param head             (optional) the head block hash
     * @param offset           (optional) how many blocks to skip after the head
     * @param reverse          (optional) whether the list should list backwards from the head
     * @param accountFilter    (optional) a list of accounts to filter by
     *
     * @see #nextPage(RequestAccountHistory, ResponseAccountHistory)
     */
    public RequestAccountHistory(String account, Integer count, String head, Integer offset, Boolean reverse,
                                 String[] accountFilter) {
        this(account, count, head, offset, null, reverse, accountFilter);
    }
    
    /**
     * @param account          the account's address
     * @param count (optional) the maximum number of transactions to fetch
     * @param head             (optional) the head block hash
     * @param offset           (optional) how many blocks to skip after the head
     * @param reverse          (optional) whether the list should list backwards from the head
     * @param accountFilter    (optional) a list of accounts to filter by
     * @param raw              (optional) if true, all block types will be returned, and the block contents will be
     *                         included
     *
     * @see #nextPage(RequestAccountHistory, ResponseAccountHistory)
     */
    public RequestAccountHistory(String account, Integer count, String head, Integer offset, Boolean raw,
                                 Boolean reverse, String[] accountFilter) {
        super("account_history", ResponseAccountHistory.class);
        this.account = account;
        this.count = count != null ? count : -1;
        this.head = head;
        this.offset = offset;
        this.reverse = reverse;
        this.accountFilter = accountFilter;
        this.raw = raw;
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
    public int getCount() {
        return count;
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
    
    /**
     * @return true if all block types should be returned
     */
    public Boolean getRawTypes() {
        return raw;
    }
    
    /**
     * Constructs the next request when using pagination.
     * @param req the previous request
     * @param res the previous response data
     * @return the next request for pagination, or null if the end has been reached
     */
    public static RequestAccountHistory nextPage(RequestAccountHistory req, ResponseAccountHistory res) {
        if (res.getSequenceBlockHash() == null) return null;
        return new RequestAccountHistory(req.account, req.count, res.getSequenceBlockHash().toHexString(), 0,
                req.raw, req.reverse, req.accountFilter);
    }
    
}
