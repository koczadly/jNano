package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.AccountMoveResponse;

public class AccountMoveRequest extends RpcRequest<AccountMoveResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String destinationWalletId;
    
    @Expose
    @SerializedName("source")
    private String sourceWalletId;
    
    @Expose
    @SerializedName("accounts")
    private String[] accounts;
    
    
    public AccountMoveRequest(String sourceWalletId, String destinationWalletId, String... accounts) {
        super("account_move", AccountMoveResponse.class);
        this.sourceWalletId = sourceWalletId;
        this.destinationWalletId = destinationWalletId;
        this.accounts = accounts;
    }
    
    
    
    public String getSourceWalletId() {
        return sourceWalletId;
    }
    
    public String getDestinationWalletId() {
        return destinationWalletId;
    }
    
    public String[] getAccounts() {
        return accounts;
    }
    
}
