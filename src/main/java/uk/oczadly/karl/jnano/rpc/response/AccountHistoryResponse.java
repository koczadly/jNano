package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.BlockType;

import java.math.BigInteger;
import java.util.Set;

public class AccountHistoryResponse extends RpcResponse {

    @Expose
    @SerializedName("history")
    private Set<AccountHistory> history;
    
    
    public Set<AccountHistory> getHistory() {
        return history;
    }
    
    
    
    
    public static class AccountHistory {
    
        @Expose
        @SerializedName("hash")
        private String blockHash;
    
        @Expose
        @SerializedName("type")
        private BlockType type;
    
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
            return type;
        }
    
        public String getAccountAddress() {
            return accountAddress;
        }
    
        public BigInteger getAmount() {
            return amount;
        }
        
    }
    
}
