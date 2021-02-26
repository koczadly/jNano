/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
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
    
    @Expose private final boolean source = true;
    
    @Expose private final String[] accounts;
    @Expose private final int count;
    @Expose private final BigInteger threshold;
    @Expose private final Boolean includeActive;
    @Expose private final Boolean sorting;
    @Expose private final Boolean includeOnlyConfirmed;
    
    
    /**
     * @param accounts the accounts' addresses
     * @param count    the limit
     */
    public RequestMultiAccountsPending(String[] accounts, int count) {
        this(accounts, count, null, null, null, true);
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
