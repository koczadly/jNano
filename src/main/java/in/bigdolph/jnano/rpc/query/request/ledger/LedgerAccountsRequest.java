package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.request.SortingOrder;
import in.bigdolph.jnano.rpc.query.response.LedgerAccountsResponse;

public class LedgerAccountsRequest extends RpcRequest<LedgerAccountsResponse> {
    
    @Expose @SerializedName("account")
    private String account;
    
    @Expose @SerializedName("count")
    private int count;
    
    @Expose @SerializedName("modified_since")
    private Long modifiedSince;
    
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
    
    public LedgerAccountsRequest(String account, int count, Long modifiedSince) {
        this(account, count, modifiedSince, SortingOrder.DEFAULT);
    }
    
    public LedgerAccountsRequest(String account, int count, Long modifiedSince, SortingOrder sortingOrder) {
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
    
    public Long getModifiedSince() {
        return modifiedSince;
    }
    
    public SortingOrder getSortingOrder() {
        return sortingOrder;
    }
    
}
