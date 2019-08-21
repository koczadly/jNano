package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.Map;

public class ResponseWalletPending extends RpcResponse {
    
    @Expose @SerializedName("blocks")
    private Map<String, Map<String, PendingBlock>> blocks;
    
    
    public Map<String, Map<String, PendingBlock>> getPendingBlocks() {
        return blocks;
    }
    
    public Map<String, PendingBlock> getPendingBlocks(String accountAddress) {
        return this.blocks.get(accountAddress.toUpperCase());
    }
    
    
    
    public static class PendingBlock {
        @Expose @SerializedName("amount")
        private BigInteger amount;
        
        @Expose @SerializedName("source")
        private String sourceAccount;
        
        
        public BigInteger getAmount() {
            return amount;
        }
        
        public String getSourceAccount() {
            return sourceAccount;
        }
    }
    
}
