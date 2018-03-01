package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.specific.AccountBlockCountResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class AccountBlockCountRequest extends RPCRequest<AccountBlockCountResponse> {
    
    @Expose
    @SerializedName("account")
    private String account;
    
    
    public AccountBlockCountRequest(String account) {
        super("account_block_count", AccountBlockCountResponse.class);
        this.account = account;
    }
    
    
    
    public String getAccount() {
        return account;
    }
}
