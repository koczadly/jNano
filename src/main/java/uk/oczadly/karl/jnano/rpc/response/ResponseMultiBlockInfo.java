package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;

import java.math.BigInteger;
import java.util.Map;

public class ResponseMultiBlockInfo extends RpcResponse {
    
    @Expose @SerializedName("blocks")
    private Map<String, BlockInfo> blocks;
    
    
    public Map<String, BlockInfo> getBlocks() {
        return blocks;
    }
    
    
    public static class BlockInfo {
        @Expose @SerializedName("block_account")
        private String account;
    
        @Expose @SerializedName("amount")
        private BigInteger amount;
    
        @Expose @SerializedName("balance")
        private BigInteger balance;
    
        @Expose @SerializedName("height")
        private long height;
    
        @Expose @SerializedName("local_timestamp")
        private long timestamp;
    
        @Expose @SerializedName("confirmed")
        private boolean confirmed;
    
        @Expose @SerializedName("contents")
        private Block blockContents;
    
        @Expose @SerializedName("subtype")
        private String subtype;
    
    
        public String getAccount() {
            return account;
        }
    
        public BigInteger getAmount() {
            return amount;
        }
    
        public BigInteger getBalance() {
            return balance;
        }
    
        public long getHeight() {
            return height;
        }
    
        public long getLocalTimestamp() {
            return timestamp;
        }
    
        public boolean isConfirmed() {
            return confirmed;
        }
    
        public Block getBlockContents() {
            return blockContents;
        }
    
        public String getSubtype() {
            return subtype;
        }
    }
    
}
