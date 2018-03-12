package in.bigdolph.jnano.rpc.query.response.specific;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.model.block.BlockType;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.math.BigInteger;
import java.util.Set;

public class AccountHistoryResponse extends RPCResponse {

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
