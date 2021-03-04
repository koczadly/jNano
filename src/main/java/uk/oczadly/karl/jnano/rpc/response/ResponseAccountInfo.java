/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import uk.oczadly.karl.jnano.internal.gsonadapters.InstantAdapter;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;

import java.time.Instant;

/**
 * This response class contains information about a single account.
 */
public class ResponseAccountInfo extends RpcResponse {
    
    @Expose private HexData frontier;
    @Expose private HexData openBlock;
    @Expose private HexData representativeBlock;
    @Expose private NanoAmount weight;
    @Expose @JsonAdapter(InstantAdapter.Seconds.class) private Instant modifiedTimestamp;
    @Expose private long blockCount;
    @Expose private NanoAccount representative;
    @Expose private NanoAmount balance;
    @Expose private NanoAmount pending;
    @Expose private long confirmationHeight;
    @Expose private HexData confirmationHeightFrontier;
    
    
    /**
     * @return the frontier (head) block hash (<strong>may be unconfirmed!</strong>)
     * @see #getConfirmationHeightFrontier()
     */
    public HexData getFrontierBlockHash() {
        return frontier;
    }
    
    /**
     * @return the open (first) block hash (<strong>may be unconfirmed!</strong>)
     */
    public HexData getOpenBlockHash() {
        return openBlock;
    }
    
    /**
     * @return the hash of the last block to set this account's representative (<strong>may be unconfirmed!</strong>)
     */
    public HexData getRepresentativeBlockHash() {
        return representativeBlock;
    }
    
    /**
     * @return the total number of blocks in this account's blockchain (<strong>contains unconfirmed blocks!</strong>)
     */
    public long getBlockCount() {
        return blockCount;
    }
    
    /**
     * @return the local timestamp when this account was last modified
     */
    public Instant getModifiedTimestamp() {
        return modifiedTimestamp;
    }
    
    /**
     * @return the address of this account's representative
     */
    public NanoAccount getRepresentativeAccount() {
        return representative;
    }
    
    /**
     * @return the total voting weight of the assigned representative
     */
    public NanoAmount getRepresentativeWeight() {
        return weight;
    }
    
    /**
     * @return the pocketed balance of this account (<strong>contains unconfirmed blocks!</strong>)
     */
    public NanoAmount getBalance() {
        return balance;
    }
    
    /**
     * @return the total pending balance of this account (<strong>contains unconfirmed blocks!</strong>)
     */
    public NanoAmount getBalancePending() {
        return pending;
    }
    
    /**
     * @return the current confirmation height for this account
     */
    public long getConfirmationHeight() {
        return confirmationHeight;
    }
    
    /**
     * @return the current confirmed frontier block hash
     */
    public HexData getConfirmationHeightFrontier() {
        return confirmationHeightFrontier;
    }
}
