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

import java.time.Instant;
import java.util.List;

/**
 * This response class contains a list of historical transactions for accounts within the wallet.
 */
public class ResponseWalletHistory extends RpcResponse {
    
    @Expose @SerializedName("history")
    private List<BlockHistory> history;
    
    
    /**
     * @return a list of transactions
     */
    public List<BlockHistory> getHistory() {
        return history;
    }
    
    
    public static class BlockHistory {
        @Expose @SerializedName("type")
        private TransactionType type;
        
        @Expose @SerializedName("account")
        private NanoAccount account;
        
        @Expose @SerializedName("amount")
        private NanoAmount amount;
        
        @Expose @SerializedName("block_account")
        private String block_account;
        
        @Expose @SerializedName("hash")
        private String hash;
        
        @Expose @SerializedName("local_timestamp") @JsonAdapter(InstantAdapter.Seconds.class)
        private Instant localTimestamp;
        
        
        /**
         * @return the type/direction of the transaction
         */
        public TransactionType getType() {
            return type;
        }
        
        /**
         * @return the address of the account
         */
        public NanoAccount getAccount() {
            return account;
        }
        
        /**
         * @return the amount being sent/received, in RAW
         */
        public NanoAmount getAmount() {
            return amount;
        }
        
        /**
         * @return the address of the account which created the block
         */
        public String getBlockAccount() {
            return block_account;
        }
        
        /**
         * @return the hash of the block
         */
        public String getHash() {
            return hash;
        }
        
        /**
         * @return the local UNIX timestamp of when the transaction took place
         */
        public Instant getLocalTimestamp() {
            return localTimestamp;
        }
    }
    
    
    public enum TransactionType {
        /**
         * Outbound transaction.
         */
        @SerializedName("send") SEND,
        
        /**
         * Inbound transaction.
         */
        @SerializedName("receive") RECEIVE
    }
    
}
