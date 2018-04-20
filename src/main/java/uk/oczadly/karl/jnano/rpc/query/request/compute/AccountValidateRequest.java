package uk.oczadly.karl.jnano.rpc.query.request.compute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.ValidationResponse;

public class AccountValidateRequest extends RpcRequest<ValidationResponse> {
    
    @Expose
    @SerializedName("account")
    private String account;
    
    
    public AccountValidateRequest(String account) {
        super("validate_account_number", ValidationResponse.class);
        this.account = account;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
}
