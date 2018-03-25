package in.bigdolph.jnano.rpc.query.request.compute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.specific.AccountValidateResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class AccountValidateRequest extends RPCRequest<AccountValidateResponse> {
    
    @Expose
    @SerializedName("account")
    private String account;
    
    
    public AccountValidateRequest(String account) {
        super("validate_account_number", AccountValidateResponse.class);
        this.account = account;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
}
