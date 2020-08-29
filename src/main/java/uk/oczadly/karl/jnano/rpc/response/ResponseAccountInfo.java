/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

/**
 * This response class contains information about a single account.
 */
public class ResponseAccountInfo extends RpcResponse {
    
    @Expose @SerializedName("frontier")
    private String frontierBlockHash;
    
    @Expose @SerializedName("open_block")
    private String openBlockHash;
    
    @Expose @SerializedName("representative_block")
    private String representativeBlockHash;
    
    @Expose @SerializedName("weight")
    private BigInteger representativeWeight;
    
    @Expose @SerializedName("modified_timestamp")
    private long modifiedTimestamp;
    
    @Expose @SerializedName("block_count")
    private long blockCount;
    
    @Expose @SerializedName("representative")
    private String representativeAccount;
    
    @Expose @SerializedName("balance")
    private BigInteger balanceConfirmed;
    
    @Expose @SerializedName("pending")
    private BigInteger balancePending;
    
    @Expose @SerializedName("confirmation_height")
    private int confirmationHeight;
    
    @Expose @SerializedName("confirmation_height_frontier")
    private String confirmationHeightFrontier;
    
    
    /**
     * @return the frontier (head) block hash
     */
    public String getFrontierBlockHash() {
        return frontierBlockHash;
    }
    
    /**
     * @return the open (first) block hash
     */
    public String getOpenBlockHash() {
        return openBlockHash;
    }
    
    /**
     * @return the hash of the last block to set this account's representative
     */
    public String getRepresentativeBlockHash() {
        return representativeBlockHash;
    }
    
    /**
     * @return the total number of blocks in this account's blockchain
     */
    public long getBlockCount() {
        return blockCount;
    }
    
    /**
     * @return the UNIX timestamp when this account was last locally modified
     */
    public long getModifiedTimestamp() {
        return modifiedTimestamp;
    }
    
    /**
     * @return the address of this account's representative
     */
    public String getRepresentativeAccount() {
        return representativeAccount;
    }
    
    /**
     * @return the total voting weight of the assigned representative
     */
    public BigInteger getRepresentativeWeight() {
        return representativeWeight;
    }
    
    /**
     * @return the confirmed balance of this account in RAW
     */
    public BigInteger getBalanceConfirmed() {
        return balanceConfirmed;
    }
    
    /**
     * @return the total pending balance of this account in RAW
     */
    public BigInteger getBalancePending() {
        return balancePending;
    }
    
    /**
     * @return the current confirmation height for this account
     */
    public int getConfirmationHeight() {
        return confirmationHeight;
    }
    
    /**
     * @return the current confirmation height block hash for this account
     */
    public String getConfirmationHeightFrontier() {
        return confirmationHeightFrontier;
    }
}
