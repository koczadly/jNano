package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.List;

public class ResponseWalletHistory extends RpcResponse {
    
    @Expose @SerializedName("history")
    private List<BlockHistory> history;
    
    
    public List<BlockHistory> getHistory() {
        return history;
    }
    
    
    
    public static class BlockHistory {
        @Expose @SerializedName("type")
        private TransactionType type;
    
        @Expose @SerializedName("account")
        private String account;
    
        @Expose @SerializedName("amount")
        private BigInteger amount;
    
        @Expose @SerializedName("block_account")
        private String block_account;
    
        @Expose @SerializedName("hash")
        private String hash;
    
        @Expose @SerializedName("local_timestamp")
        private int localTimestamp;
    
    
        public TransactionType getType() {
            return type;
        }
    
        public String getAccount() {
            return account;
        }
    
        public BigInteger getAmount() {
            return amount;
        }
    
        public String getBlock_account() {
            return block_account;
        }
    
        public String getHash() {
            return hash;
        }
    
        public int getLocalTimestamp() {
            return localTimestamp;
        }
    }
    
    
    public enum TransactionType {
        @SerializedName("send")     SEND,
        @SerializedName("receive")  RECEIVE;
    }
    
}
