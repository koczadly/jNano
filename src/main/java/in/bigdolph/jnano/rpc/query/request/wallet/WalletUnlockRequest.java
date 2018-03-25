package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.generic.ValidatedResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class WalletUnlockRequest extends RPCRequest<ValidatedResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    @Expose
    @SerializedName("password")
    private String password;
    
    
    public WalletUnlockRequest(String walletId, String password) {
        super("password_enter", ValidatedResponse.class);
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
