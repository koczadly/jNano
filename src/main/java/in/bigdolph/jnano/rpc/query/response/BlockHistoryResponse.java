package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.model.block.BlockType;

import java.math.BigInteger;
import java.util.Set;

public class BlockHistoryResponse extends RpcResponse {
    
    @Expose
    @SerializedName("history")
    private Set<TransactionalBlock> blocks;
    
    
    
    public Set<TransactionalBlock> getBlocks() {
        return blocks;
    }
    
    
    
    public static class TransactionalBlock {
        
        @Expose
        @SerializedName("hash")
        private String blockHash;
    
        @Expose
        @SerializedName("type")
        private BlockType blockType;
    
        @Expose
        @SerializedName("account")
        private String accountAddress;
    
        @Expose
        @SerializedName("amount")
        private BigInteger amount;
        
    
        public String getBlockHash() {
            return blockHash;
        }
    
        public BlockType getBlockType() {
            return blockType;
        }
    
        public String getAccountAddress() {
            return accountAddress;
        }
    
        public BigInteger getAmount() {
            return amount;
        }
        
    }
    
}
