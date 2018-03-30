package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.AccountResponse;

public class AccountCreateRequest extends RpcRequest<AccountResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    @Expose
    @SerializedName("work")
    private boolean generateWork;
    
    
    public AccountCreateRequest(String walletId) {
        this(walletId, true);
    }
    
    public AccountCreateRequest(String walletId, boolean generateWork) {
        super("account_create", AccountResponse.class);
        this.walletId = walletId;
        this.generateWork = generateWork;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
    public boolean getGenerateWork() {
        return generateWork;
    }
    
}
