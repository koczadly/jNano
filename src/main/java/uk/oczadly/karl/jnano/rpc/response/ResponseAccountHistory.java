/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.gsonadapters.InstantAdapter;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.BlockType;

import java.time.Instant;
import java.util.List;

/**
 * This response class contains historical information about an account.
 */
public class ResponseAccountHistory extends RpcResponse {
    
    @Expose @SerializedName("account")  private NanoAccount account;
    @Expose @SerializedName("history")  private List<BlockInfo> history;
    @Expose @SerializedName("previous") private String previousHash;
    @Expose @SerializedName("next")     private String nextHash;
    
    
    /**
     * @return the account's address
     */
    public NanoAccount getAccount() {
        return account;
    }
    
    /**
     * Returns a list of historical blocks.
     */
    public List<BlockInfo> getHistory() {
        return history;
    }
    
    /**
     * @return the previous block hash, or null if traversing backwards
     *
     * @see #getNextBlockHash()
     */
    public String getPreviousBlockHash() {
        return previousHash;
    }
    
    /**
     * @return the next block hash, or null if traversing forwards
     *
     * @see #getPreviousBlockHash()
     */
    public String getNextBlockHash() {
        return nextHash;
    }
    
    /**
     * @return the previous or next block hash in the search sequence, or null
     * @see #getNextBlockHash()
     * @see #getPreviousBlockHash()
     */
    public String getSequenceBlockHash() {
        return previousHash != null ? previousHash : nextHash;
    }
    
    
    public static class BlockInfo {
        @Expose @SerializedName("type")             private BlockType type;
        @Expose @SerializedName("account")          private NanoAccount account;
        @Expose @SerializedName("amount")           private NanoAmount amount;
        @Expose @SerializedName("local_timestamp") @JsonAdapter(InstantAdapter.Seconds.class)
        private Instant timestamp;
        @Expose @SerializedName("height")           private int height;
        @Expose @SerializedName("hash")             private String hash;
    
    
        public BlockType getType() {
            return type;
        }
    
        public NanoAccount getAccount() {
            return account;
        }
    
        public NanoAmount getAmount() {
            return amount;
        }
    
        public Instant getTimestamp() {
            return timestamp;
        }
    
        public int getHeight() {
            return height;
        }
    
        public String getHash() {
            return hash;
        }
    }
    
}
