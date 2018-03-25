package in.bigdolph.jnano.rpc.query.response.specific;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.model.block.Block;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.math.BigInteger;
import java.util.Map;

public class AccountPendingBlocksResponse extends RPCResponse {
    
    @Expose
    @SerializedName("blocks")
    private Map<String, PendingBlock> blocks;
    
    
    /** Hash -> Block info */
    public Map<String, PendingBlock> getPendingBlocks() {
        return blocks;
    }
    
    public PendingBlock getPendingBlock(String blockHash) {
        return this.blocks.get(blockHash.toUpperCase());
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
