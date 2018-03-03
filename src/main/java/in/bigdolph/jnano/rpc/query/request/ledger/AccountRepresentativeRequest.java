package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.specific.AccountRepresentativeResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class AccountRepresentativeRequest extends RPCRequest<AccountRepresentativeResponse> {
    
    @Expose
    @SerializedName("account")
    private String account;
    
    
    public AccountRepresentativeRequest(String account) {
        super("account_representative", AccountRepresentativeResponse.class);
        this.account = account;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
}
