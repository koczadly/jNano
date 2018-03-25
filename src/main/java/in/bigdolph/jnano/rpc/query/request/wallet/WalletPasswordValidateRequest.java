package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.generic.ValidationResponse;

public class WalletPasswordValidateRequest extends RPCRequest<ValidationResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    
    public WalletPasswordValidateRequest(String walletId) {
        super("password_valid", ValidationResponse.class);
        this.walletId = walletId;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
