package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;
import in.bigdolph.jnano.rpc.query.response.WalletChangeResponse;

public class WalletChangePasswordRequest extends RPCRequest<WalletChangeResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    @Expose
    @SerializedName("password")
    private String password;
    
    
    public WalletChangePasswordRequest(String walletId, String password) {
        super("password_change", WalletChangeResponse.class);
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
