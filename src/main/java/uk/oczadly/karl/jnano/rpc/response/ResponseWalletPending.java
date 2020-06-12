package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.AccountAddress;

import java.math.BigInteger;
import java.util.Map;

/**
 * This response class contains a set of pending blocks for a wallet.
 */
public class ResponseWalletPending extends RpcResponse {
    
    @Expose @SerializedName("blocks")
    private Map<AccountAddress, Map<String, PendingBlock>> blocks;
    
    
    /**
     * Map follows the structure {@code account address -> block hash -> block details}.
     *
     * @return a map of pending blocks
     */
    public Map<AccountAddress, Map<String, PendingBlock>> getPendingBlocks() {
        return blocks;
    }
    
    /**
     * Map follows the structure {@code block hash -> block details}.
     *
     * @param accountAddress a local account's address
     * @return a map of pending blocks, or null if not present in the response
     */
    public Map<String, PendingBlock> getPendingBlocks(AccountAddress accountAddress) {
        return this.blocks.get(accountAddress);
    }
    
    
    public static class PendingBlock {
        @Expose @SerializedName("amount")
        private BigInteger amount;
        
        @Expose @SerializedName("source")
        private AccountAddress sourceAccount;
        
        
        /**
         * @return the pending amount, in RAW
         */
        public BigInteger getAmount() {
            return amount;
        }
        
        /**
         * @return the address of the sending account of this block
         */
        public AccountAddress getSourceAccount() {
            return sourceAccount;
        }
        
    }
    
}
