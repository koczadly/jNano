package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcResponse;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This response class contains a set of pending blocks.
 */
public class ResponsePending extends RpcResponse {
    
    @Expose @SerializedName("blocks")
    private LinkedHashMap<String, PendingBlock> blocks;
    
    
    /**
     * Map follows the structure {@code block hash -> block info}.
     *
     * @return a map of representatives
     */
    public Map<String, PendingBlock> getPendingBlocks() {
        return blocks;
    }
    
    /**
     * @param blockHash a pending block's hash
     * @return information about the specified pending block, or null if not present in the response
     */
    public PendingBlock getPendingBlock(String blockHash) {
        return this.blocks.get(blockHash.toUpperCase());
    }
    
    
    public static class PendingBlock {
        @Expose @SerializedName("amount")
        private BigInteger amount;
        
        @Expose @SerializedName("source")
        private String sourceAccount;
        
        
        /**
         * @return the pending amount, in RAW
         */
        public BigInteger getAmount() {
            return amount;
        }
        
        /**
         * @return the sending account of this block
         */
        public String getSourceAccount() {
            return sourceAccount;
        }
        
    }
    
}
