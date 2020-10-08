/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This response class contains a set of pending blocks.
 */
public class ResponsePending extends RpcResponse {
    
    @Expose @SerializedName("blocks")
    private LinkedHashMap<HexData, PendingBlock> blocks;
    
    
    /**
     * Map follows the structure {@code block hash -> block info}.
     *
     * @return a map of representatives
     */
    public Map<HexData, PendingBlock> getPendingBlocks() {
        return blocks;
    }
    
    /**
     * @param blockHash a pending block's hash
     * @return information about the specified pending block, or null if not present in the response
     */
    public PendingBlock getPendingBlock(String blockHash) {
        return getPendingBlock(new HexData(blockHash));
    }
    
    /**
     * @param blockHash a pending block's hash
     * @return information about the specified pending block, or null if not present in the response
     */
    public PendingBlock getPendingBlock(HexData blockHash) {
        return blocks.get(blockHash);
    }
    
    
    public static class PendingBlock {
        @Expose @SerializedName("amount")
        private NanoAmount amount;
        
        @Expose @SerializedName("source")
        private NanoAccount sourceAccount;
        
        
        /**
         * @return the pending amount, in RAW
         */
        public NanoAmount getAmount() {
            return amount;
        }
        
        /**
         * @return the sending account of this block
         */
        public NanoAccount getSourceAccount() {
            return sourceAccount;
        }
        
    }
    
}
