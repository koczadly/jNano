package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.AccountResponse;

public class WalletAddAccountRequest extends RpcRequest<AccountResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    @Expose
    @SerializedName("key")
    private String key;
    
    @Expose
    @SerializedName("work")
    private boolean generateWork;
    
    
    public WalletAddAccountRequest(String walletId, String key) {
        this(walletId, key, true);
    }
    
    public WalletAddAccountRequest(String walletId, String key, boolean generateWork) {
        super("wallet_add", AccountResponse.class);
        this.walletId = walletId;
        this.key = key;
        this.generateWork = generateWork;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
    public String getKey() {
        return key;
    }
    
    public boolean getGenerateWork() {
        return generateWork;
    }
    
}
