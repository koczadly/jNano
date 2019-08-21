package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseValidation;

public class RequestPasswordValid extends RpcRequest<ResponseValidation> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    
    public RequestPasswordValid(String walletId) {
        super("password_valid", ResponseValidation.class);
        this.walletId = walletId;
    }
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
