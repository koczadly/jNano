package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.AccountsResponse;

public class AccountListRequest extends RPCRequest<AccountsResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    
    public AccountListRequest(String walletId) {
        super("account_list", AccountsResponse.class);
        this.walletId = walletId;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
