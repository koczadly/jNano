package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.ValidationResponse;

public class WalletUnlockRequest extends RpcRequest<ValidationResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    @Expose
    @SerializedName("password")
    private String password;
    
    
    public WalletUnlockRequest(String walletId, String password) {
        super("password_enter", ValidationResponse.class);
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
