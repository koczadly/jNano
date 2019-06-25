package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

public class AccountsPendingResponse extends RpcResponse {

    @Expose
    @SerializedName("blocks")
    private Map<String, Map<String, PendingBlock>> blocks;
    
    
    
    /** Address -&gt; Block hash -&gt; Block details */
    public Map<String, Map<String, PendingBlock>> getPendingBlocks() {
        return blocks;
    }
    
    public Map<String, PendingBlock> getPendingBlocks(String accountAddress) {
        return blocks.get(accountAddress.toLowerCase());
    }
    
    public Set<String> getPendingBlockHashes(String accountAddress) {
        Map<String, PendingBlock> accountBlocks = this.getPendingBlocks(accountAddress);
        return accountBlocks != null ? accountBlocks.keySet() : null;
    }
    
    
    
    public static class PendingBlock {
        
        @Expose
        @SerializedName("amount")
        private BigInteger amount;
    
        @Expose
        @SerializedName("source")
        private String sourceAccount;
    
    
        public BigInteger getAmount() {
            return amount;
        }
    
        public String getSourceAccount() {
            return sourceAccount;
        }
        
    }
    
}
