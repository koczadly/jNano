package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseLedger;

import java.math.BigInteger;

public class RequestLedger extends RpcRequest<ResponseLedger> {
    
    @Expose @SerializedName("representative")
    private final boolean fetchRepresentative = true;
    
    @Expose @SerializedName("weight")
    private final boolean fetchWeight = true;
    
    @Expose @SerializedName("pending")
    private final boolean fetchPending = true;
    
    
    @Expose @SerializedName("account")
    private String account;
    
    @Expose @SerializedName("count")
    private int count;
    
    
    @Expose @SerializedName("modified_since")
    private Integer modifiedSince;
    
    @Expose @SerializedName("sorting")
    private Boolean sorting;
    
    @Expose @SerializedName("threshold")
    private BigInteger thresholdBalance;
    
    
    public RequestLedger(String account, int count) {
        this(account, count, null, null, null);
    }
    
    public RequestLedger(String account, int count, Integer modifiedSince, Boolean sorting, BigInteger thresholdBalance) {
        super("ledger", ResponseLedger.class);
        this.account = account;
        this.count = count;
        this.modifiedSince = modifiedSince;
        this.sorting = sorting;
        this.thresholdBalance = thresholdBalance;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
    public int getCount() {
        return count;
    }
    
    public Integer getModifiedSince() {
        return modifiedSince;
    }
    
    public Boolean getSorting() {
        return sorting;
    }
    
    public BigInteger getThresholdBalance() {
        return thresholdBalance;
    }
    
}
