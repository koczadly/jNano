package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponsePending;

import java.math.BigInteger;

public class RequestPending extends RpcRequest<ResponsePending> {
    
    @Expose @SerializedName("source")
    private final boolean fetchSource = true;
    
    
    @Expose @SerializedName("account")
    private String account;
    
    
    @Expose @SerializedName("count")
    private int count;
    
    @Expose @SerializedName("threshold")
    private BigInteger threshold;
    
    @Expose @SerializedName("include_active")
    private Boolean includeActive;
    
    @Expose @SerializedName("sorting")
    private Boolean sorting;
    
    @Expose @SerializedName("include_only_confirmed")
    private Boolean includeOnlyConfirmed;
    
    
    public RequestPending(String account, int count) {
        this(account, count, null, null, null, null);
    }
    
    public RequestPending(String account, int count, BigInteger threshold, Boolean includeActive, Boolean sorting,
                          Boolean includeOnlyConfirmed) {
        super("pending", ResponsePending.class);
        this.account = account;
        this.count = count;
        this.threshold = threshold;
        this.includeActive = includeActive;
        this.sorting = sorting;
        this.includeOnlyConfirmed = includeOnlyConfirmed;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
    public int getCount() {
        return count;
    }
    
    public BigInteger getThreshold() {
        return threshold;
    }
    
    public Boolean getIncludeActive() {
        return includeActive;
    }
    
    public Boolean getSorting() {
        return sorting;
    }
    
    public Boolean getIncludeOnlyConfirmed() {
        return includeOnlyConfirmed;
    }
}
