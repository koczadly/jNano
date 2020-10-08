/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.gsonadapters.InstantAdapter;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.block.Block;

import java.time.Instant;
import java.util.Set;

/**
 * This response class contains a set of unchecked keys and blocks.
 */
public class ResponseUncheckedKeys extends RpcResponse {
    
    @Expose @SerializedName("unchecked")
    private Set<UncheckedBlock> blocks;
    
    
    /**
     * @return a set of unchecked block information
     */
    public Set<UncheckedBlock> getUncheckedBlocks() {
        return blocks;
    }
    
    
    public static class UncheckedBlock {
        @Expose @SerializedName("key")
        private HexData key;
        
        @Expose @SerializedName("hash")
        private HexData blockHash;
        
        @Expose @SerializedName("modified_timestamp") @JsonAdapter(InstantAdapter.Seconds.class)
        private Instant modifiedTimestamp;
        
        @Expose @SerializedName("contents")
        private Block block;
        
        
        /**
         * @return the key
         */
        public HexData getKey() {
            return key;
        }
        
        /**
         * @return the block's hash
         */
        public HexData getBlockHash() {
            return blockHash;
        }
        
        /**
         * @return the UNIX timestamp when this block was last modified/processed
         */
        public Instant getModifiedTimestamp() {
            return modifiedTimestamp;
        }
        
        /**
         * @return the block's contents
         */
        public Block getBlock() {
            return block;
        }
    }
    
}
