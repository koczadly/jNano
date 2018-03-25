package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.request.SortingOrder;
import in.bigdolph.jnano.rpc.query.response.specific.LedgerAccountsResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class LedgerAccountsRequest extends RPCRequest<LedgerAccountsResponse> {
    
    @Expose
    @SerializedName("account")
    private String account;
    
    @Expose
    @SerializedName("count")
    private int transactionCount;
    
    @Expose
    @SerializedName("representative")
    private boolean fetchRepresentative = true;
    
    @Expose
    @SerializedName("weight")
    private boolean fetchWeight = true;
    
    @Expose
    @SerializedName("pending")
    private boolean fetchPending = true;
    
    @Expose
    @SerializedName("sorting")
    private SortingOrder order;
    
    
    public LedgerAccountsRequest(String account, int transactionCount) {
        this(account, transactionCount, SortingOrder.DEFAULT);
    }
    
    public LedgerAccountsRequest(String account, int transactionCount, SortingOrder order) {
        super("ledger", LedgerAccountsResponse.class);
        this.account = account;
        this.transactionCount = transactionCount;
        this.order = order;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
    public int getTransactionCount() {
        return transactionCount;
    }
    
}
