package uk.oczadly.karl.jnano.rpc.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.request.SortingOrder;
import uk.oczadly.karl.jnano.rpc.response.LedgerAccountsResponse;

public class LedgerAccountsRequest extends RpcRequest<LedgerAccountsResponse> {
    
    @Expose @SerializedName("account")
    private String account;
    
    @Expose @SerializedName("count")
    private int count;
    
    @Expose @SerializedName("modified_since")
    private Integer modifiedSince;
    
    @Expose @SerializedName("sorting")
    private SortingOrder sortingOrder;
    
    @Expose @SerializedName("representative")
    private boolean fetchRepresentative = true;
    
    @Expose @SerializedName("weight")
    private boolean fetchWeight = true;
    
    @Expose @SerializedName("pending")
    private boolean fetchPending = true;
    
    
    public LedgerAccountsRequest(String account, int count) {
        this(account, count, null);
    }
    
    public LedgerAccountsRequest(String account, int count, Integer modifiedSince) {
        this(account, count, modifiedSince, SortingOrder.DEFAULT);
    }
    
    public LedgerAccountsRequest(String account, int count, Integer modifiedSince, SortingOrder sortingOrder) {
        super("ledger", LedgerAccountsResponse.class);
        this.account = account;
        this.count = count;
        this.modifiedSince = modifiedSince;
        this.sortingOrder = sortingOrder;
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
    
    public SortingOrder getSortingOrder() {
        return sortingOrder;
    }
    
}
