package uk.oczadly.karl.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.AccountFrontiersResponse;

public class AccountFrontiersRequest extends RpcRequest<AccountFrontiersResponse> {
    
    @Expose
    @SerializedName("account")
    private String account;
    
    @Expose
    @SerializedName("count")
    private int count;
    
    
    public AccountFrontiersRequest(String account, int count) {
        super("frontiers", AccountFrontiersResponse.class);
        this.account = account;
        this.count = count;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
    public int getCount() {
        return count;
    }
    
}
