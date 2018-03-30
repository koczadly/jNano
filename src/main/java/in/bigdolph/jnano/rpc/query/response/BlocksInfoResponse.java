package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.model.block.Block;

import java.math.BigInteger;
import java.util.Map;

public class BlocksInfoResponse extends RpcResponse {
    
    @Expose
    @SerializedName("blocks")
    private Map<String, BlockInfo> blocks;
    
    
    /** Hash -> Block info */
    public Map<String, BlockInfo> getBlocks() {
        return blocks;
    }
    
    public BlockInfo getBlock(String blockHash) {
        return this.blocks.get(blockHash.toUpperCase());
    }
    
    
    
    public static class BlockInfo {
    
        @Expose
        @SerializedName("contents")
        private Block block;
        
        @Expose
        @SerializedName("block_account")
        private String blockAccount;
    
        @Expose
        @SerializedName("source_account")
        private String sourceAccount;
    
        @Expose
        @SerializedName("amount")
        private BigInteger amount;
    
        @Expose
        @SerializedName("pending")
        private BigInteger pending;
        
    
        public Block getBlock() {
            return block;
        }
    
        public String getBlockAccount() {
            return blockAccount;
        }
    
        public String getSourceAccount() {
            return sourceAccount;
        }
    
        public BigInteger getAmount() {
            return amount;
        }
    
        public BigInteger getPending() {
            return pending;
        }
        
    }
    
}
