package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.generic.AccountResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class AccountCreateRequest extends RPCRequest<AccountResponse> {
    
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
