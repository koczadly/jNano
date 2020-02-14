package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.rpc.RpcResponse;

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
        private String key;
    
        @Expose @SerializedName("hash")
        private String blockHash;
    
        @Expose @SerializedName("modified_timestamp")
        private int modifiedTimestamp;
    
        @Expose @SerializedName("contents")
        private Block block;
    
    
        /**
         * @return the key (hash)
         */
        public String getKey() {
            return key;
        }
    
        /**
         * @return the block's hash
         */
        public String getBlockHash() {
            return blockHash;
        }
    
        /**
         * @return the UNIX timestamp when this block was last modified/processed
         */
        public int getModifiedTimestamp() {
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
