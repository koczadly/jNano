package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccountHistory;

public class RequestAccountHistory extends RpcRequest<ResponseAccountHistory> {
    
    @Expose @SerializedName("account")
    private String account;
    
    @Expose @SerializedName("count")
    private int transactionCount;
    
    @Expose @SerializedName("raw")
    private boolean raw = true;
    
    @Expose @SerializedName("head")
    private String head;
    
    @Expose @SerializedName("offset")
    private int offset;
    
    @Expose @SerializedName("reverse")
    private boolean reverse;
    
    @Expose @SerializedName("account_filter")
    private String[] accountFilter;
    
    
    public RequestAccountHistory(String account, int transactionCount) {
        this(account, transactionCount, null, null, null, null);
    }
    
    public RequestAccountHistory(String account, int transactionCount, String head, Integer offset, Boolean reverse, String[] accountFilter) {
        super("account_history", ResponseAccountHistory.class);
        this.account = account;
        this.transactionCount = transactionCount;
        this.head = head;
        this.offset = offset;
        this.reverse = reverse;
        this.accountFilter = accountFilter;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
    public int getTransactionCount() {
        return transactionCount;
    }
    
}
