package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseValidation;

public class RequestPasswordEnter extends RpcRequest<ResponseValidation> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    @Expose @SerializedName("password")
    private String password;
    
    
    public RequestPasswordEnter(String walletId, String password) {
        super("password_enter", ResponseValidation.class);
        this.walletId = walletId;
        this.password = password;
    }
    
    
    public String getWalletId() {
        return walletId;
    }
    
    public String getPassword() {
        return password;
    }
    
}
