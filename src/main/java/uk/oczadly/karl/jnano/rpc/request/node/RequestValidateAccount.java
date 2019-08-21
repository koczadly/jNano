package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseValidation;

public class RequestValidateAccount extends RpcRequest<ResponseValidation> {
    
    @Expose
    @SerializedName("account")
    private String account;
    
    
    public RequestValidateAccount(String account) {
        super("validate_account_number", ResponseValidation.class);
        this.account = account;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
}
