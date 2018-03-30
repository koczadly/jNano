package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedHashMap;

public class LedgerAccountsResponse extends RpcResponse {
    
    @Expose
    @SerializedName("accounts")
    private LinkedHashMap<String, AccountInfo> accounts;
    
    
    public LinkedHashMap<String, AccountInfo> getAccounts() {
        return accounts;
    }
    
    public AccountInfo getAccount(String account) {
        return accounts.get(account.toLowerCase());
    }
    
    
    
    public static class AccountInfo {
        
        @Expose
        @SerializedName("frontier")
        private String frontierBlockHash;
    
        @Expose
        @SerializedName("open_block")
        private String openBlockHash;
    
        @Expose
        @SerializedName("representative_block")
        private String representativeBlockHash;
    
        @Expose
        @SerializedName("balance")
        private BigInteger balance;
    
        @Expose
        @SerializedName("modified_timestamp")
        private int modifiedTimestamp;
        private Date modifiedDate;
    
        @Expose
        @SerializedName("block_count")
        private int blockCount;
    
        @Expose
        @SerializedName("representative")
        private String representativeAccount;
    
        @Expose
        @SerializedName("weight")
        private BigInteger votingWeight;
    
        @Expose
        @SerializedName("pending")
        private BigInteger balancePending;
    
    
        public String getFrontierBlockHash() {
            return frontierBlockHash;
        }
    
        public String getOpenBlockHash() {
            return openBlockHash;
        }
    
        public String getRepresentativeBlockHash() {
            return representativeBlockHash;
        }
        
        public BigInteger getBalance() {
            return balance;
        }
    
        public BigInteger getBalancePending() {
            return balancePending;
        }
    
        public int getModifiedTimestampSeconds() {
            return modifiedTimestamp;
        }
    
        public Date getModifiedTimestamp() {
            if(modifiedDate == null) modifiedDate = new Date(modifiedTimestamp * 1000);
            return modifiedDate;
        }
    
        public String getRepresentativeAccount() {
            return representativeAccount;
        }
    
        public BigInteger getVotingWeight() {
            return votingWeight;
        }
    
        public int getBlockCount() {
            return blockCount;
        }
        
    }
    
}
