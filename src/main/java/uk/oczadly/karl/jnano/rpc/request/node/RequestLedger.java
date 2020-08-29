/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseLedger;

import java.math.BigInteger;

/**
 * This request class is used to request detailed account information from the ledger.
 * <br>Calls the RPC command {@code ledger}, and returns a {@link ResponseLedger} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#ledger">Official RPC documentation</a>
 */
public class RequestLedger extends RpcRequest<ResponseLedger> {
    
    @Expose @SerializedName("representative")
    private final boolean fetchRepresentative = true;
    
    @Expose @SerializedName("weight")
    private final boolean fetchWeight = true;
    
    @Expose @SerializedName("pending")
    private final boolean fetchPending = true;
    
    
    @Expose @SerializedName("account")
    private final String account;
    
    @Expose @SerializedName("count")
    private final int count;
    
    
    @Expose @SerializedName("modified_since")
    private final Integer modifiedSince;
    
    @Expose @SerializedName("sorting")
    private final Boolean sorting;
    
    @Expose @SerializedName("threshold")
    private final BigInteger thresholdBalance;
    
    
    /**
     * @param account the address to start from, or null to begin from the zeroth account
     * @param count   the response limit
     */
    public RequestLedger(String account, int count) {
        this(account, count, null, null, null);
    }
    
    /**
     * @param account          the address to start from, or null to begin from the zeroth account
     * @param count            the response limit
     * @param modifiedSince    (optional) filter accounts modified after the specified UNIX timestamp
     * @param sorting          (optional) whether the accounts should be sorted in descending order (WARNING: 'count' is
     *                         ignored if specified)
     * @param thresholdBalance (optional) the minimum threshold balance for listed accounts
     */
    public RequestLedger(String account, int count, Integer modifiedSince, Boolean sorting,
                         BigInteger thresholdBalance) {
        super("ledger", ResponseLedger.class);
        this.account = account != null ? account : NanoAccount.ZERO_ACCOUNT.toAddress();
        this.count = count;
        this.modifiedSince = modifiedSince;
        this.sorting = sorting;
        this.thresholdBalance = thresholdBalance;
    }
    
    
    /**
     * @return the requested starting account
     */
    public String getAccount() {
        return account;
    }
    
    /**
     * @return the requested limit
     */
    public int getCount() {
        return count;
    }
    
    /**
     * @return the requested threshold modification timestamp
     */
    public Integer getModifiedSince() {
        return modifiedSince;
    }
    
    /**
     * @return the requested sorting option
     */
    public Boolean getSorting() {
        return sorting;
    }
    
    /**
     * @return the requested minimum threshold balance
     */
    public BigInteger getThresholdBalance() {
        return thresholdBalance;
    }
    
}
