package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseMultiAccountsPending;

import java.math.BigInteger;

public class RequestMultiAccountsPending extends RpcRequest<ResponseMultiAccountsPending> {
    
    @Expose @SerializedName("accounts")
    private String[] accounts;
    
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
    
    @Expose @SerializedName("source")
    private boolean source = true;
    
    
    public RequestMultiAccountsPending(String[] accounts, int count) {
        this(accounts, count, null, null, null, null);
    }
    
    public RequestMultiAccountsPending(String[] accounts, int count, BigInteger threshold, Boolean includeActive, Boolean sorting, Boolean includeOnlyConfirmed) {
        super("accounts_pending", ResponseMultiAccountsPending.class);
        this.accounts = accounts;
        this.count = count;
        this.threshold = threshold;
        this.includeActive = includeActive;
        this.sorting = sorting;
        this.includeOnlyConfirmed = includeOnlyConfirmed;
    }
    
    
    
    public String[] getAccounts() {
        return accounts;
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
