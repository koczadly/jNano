package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.AccountAddress;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * This response class contains a set pending blocks for multiple accounts.
 */
public class ResponseMultiAccountsPending extends RpcResponse {
    
    @Expose @SerializedName("blocks")
    private Map<AccountAddress, LinkedHashMap<String, PendingBlock>> blocks;
    
    
    /**
     * Map follows the structure {@code address -> block hash -> block details}.
     *
     * @return a map of pending blocks
     */
    public Map<AccountAddress, LinkedHashMap<String, PendingBlock>> getPendingBlocks() {
        return blocks;
    }
    
    /**
     * Map follows the structure {@code block hash -> block details}.
     *
     * @param accountAddress an account's address
     * @return a map of pending blocks for the specified account, or null if not present in the response
     */
    public Map<String, PendingBlock> getPendingBlocks(AccountAddress accountAddress) {
        return blocks.get(accountAddress);
    }
    
    /**
     * @param accountAddress an account's address
     * @return a set of pending block hashes for the specified account, or null if not present in the response
     */
    public Set<String> getPendingBlockHashes(AccountAddress accountAddress) {
        Map<String, PendingBlock> accountBlocks = this.getPendingBlocks(accountAddress);
        return accountBlocks != null ? accountBlocks.keySet() : null;
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
