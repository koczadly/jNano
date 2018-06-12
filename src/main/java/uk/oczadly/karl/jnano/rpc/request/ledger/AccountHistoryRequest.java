package uk.oczadly.karl.jnano.rpc.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.AccountHistoryResponse;

public class AccountHistoryRequest extends RpcRequest<AccountHistoryResponse> {
    
    @Expose
    @SerializedName("account")
    private String account;
    
    @Expose
    @SerializedName("count")
    private int transactionCount;
    
    @Expose
    @SerializedName("raw")
    private boolean fetchRaw = true;
    
    
    public AccountHistoryRequest(String account, int transactionCount) {
        super("account_history", AccountHistoryResponse.class);
        this.account = account;
        this.transactionCount = transactionCount;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
    public int getTransactionCount() {
        return transactionCount;
    }
    
}
