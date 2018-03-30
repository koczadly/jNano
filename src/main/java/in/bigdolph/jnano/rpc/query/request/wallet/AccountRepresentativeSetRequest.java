package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.BlockHashResponse;

public class AccountRepresentativeSetRequest extends RPCRequest<BlockHashResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    @Expose
    @SerializedName("account")
    private String account;
    
    @Expose
    @SerializedName("representative")
    private String representativeAccount;
    
    @Expose
    @SerializedName("work")
    private String workSolution;
    
    
    public AccountRepresentativeSetRequest(String walletId, String account, String representativeAccount) {
        this(walletId, account, representativeAccount, null);
    }
    
    public AccountRepresentativeSetRequest(String walletId, String account, String representativeAccount, String workSolution) {
        super("account_representative_set", BlockHashResponse.class);
        this.walletId = walletId;
        this.account = account;
        this.representativeAccount = representativeAccount;
        this.workSolution = workSolution;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
    public String getAccount() {
        return account;
    }
    
    public String getRepresentativeAccount() {
        return representativeAccount;
    }
    
    public String getWorkSolution() {
        return workSolution;
    }
}
