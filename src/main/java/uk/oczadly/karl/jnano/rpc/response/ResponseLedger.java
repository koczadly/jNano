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
import java.util.LinkedHashMap;

/**
 * This response class contains a set of accounts in the ledger.
 */
public class ResponseLedger extends RpcResponse {
    
    @Expose private LinkedHashMap<NanoAccount, AccountInfo> accounts;
    
    
    /**
     * Map follows the structure {@code account address -> information}.
     *
     * @return a map of accounts and their details
     */
    public LinkedHashMap<NanoAccount, AccountInfo> getAccounts() {
        return accounts;
    }
    
    /**
     * @param account an account's address
     * @return the account's details
     */
    public AccountInfo getAccount(NanoAccount account) {
        return accounts.get(account);
    }
    
    
    public static class AccountInfo {
        @Expose private HexData frontier;
        @Expose private HexData openBlock;
        @Expose private HexData representativeBlock;
        @Expose private NanoAmount weight;
        @Expose @JsonAdapter(InstantAdapter.Seconds.class) private Instant modifiedTimestamp;
        @Expose private long blockCount;
        @Expose private NanoAccount representative;
        @Expose private NanoAmount balance;
        @Expose private NanoAmount pending;
        
        /**
         * @return the frontier (head) block hash
         */
        public HexData getFrontierBlockHash() {
            return frontier;
        }
        
        /**
         * @return the open (first) block hash
         */
        public HexData getOpenBlockHash() {
            return openBlock;
        }
        
        /**
         * @return the hash of the last block to set this account's representative
         */
        public HexData getRepresentativeBlockHash() {
            return representativeBlock;
        }
        
        /**
         * @return the total number of blocks in this account's blockchain
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
         * @return the total voting weight delegated to this account
         */
        public NanoAmount getVotingWeight() {
            return weight;
        }
        
        /**
         * @return the confirmed balance of this account in RAW
         */
        public NanoAmount getBalanceConfirmed() {
            return balance;
        }
        
        /**
         * @return the total pending balance of this account in RAW
         */
        public NanoAmount getBalancePending() {
            return pending;
        }
    }
    
}
