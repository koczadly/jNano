package uk.oczadly.karl.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class AccountInformationResponse extends RpcResponse {

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
    @SerializedName("weight")
    private BigInteger representativeWeight;
    
    @Expose
    @SerializedName("modified_timestamp")
    private long modifiedTimestamp;
    
    @Expose
    @SerializedName("block_count")
    private long blockCount;
    
    @Expose
    @SerializedName("representative")
    private String representativeAccount;
    
    @Expose
    @SerializedName("balance")
    private BigInteger balanceConfirmed;
    
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
    
    public long getBlockCount() {
        return blockCount;
    }
    
    public long getModifiedTimestamp() {
        return modifiedTimestamp;
    }
    
    public String getRepresentativeAccount() {
        return representativeAccount;
    }
    
    public BigInteger getRepresentativeWeight() {
        return representativeWeight;
    }
    
    public BigInteger getBalanceConfirmed() {
        return balanceConfirmed;
    }
    
    public BigInteger getBalancePending() {
        return balancePending;
    }
    
}
