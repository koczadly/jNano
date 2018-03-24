package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.generic.AccountFrontiersResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class AccountFrontiersRequest extends RPCRequest<AccountFrontiersResponse> {
    
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
