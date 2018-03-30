package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

public class WalletPendingBlocksResponse extends RPCResponse {
    
    @Expose
    @SerializedName("blocks")
    private Map<String, Map<String, PendingBlock>> blocks;
    
    
    /** Account -> block hash -> block info */
    public Map<String, Map<String, PendingBlock>> getPendingBlocks() {
        return blocks;
    }
    
    public Map<String, PendingBlock> getPendingBlocks(String accountAddress) {
        return this.blocks.get(accountAddress.toUpperCase());
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
