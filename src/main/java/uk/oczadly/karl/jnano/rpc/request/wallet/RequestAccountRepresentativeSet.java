package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHash;

public class RequestAccountRepresentativeSet extends RpcRequest<ResponseBlockHash> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    @Expose @SerializedName("account")
    private String account;
    
    @Expose @SerializedName("representative")
    private String representativeAccount;
    
    @Expose @SerializedName("work")
    private String workSolution;
    
    
    public RequestAccountRepresentativeSet(String walletId, String account, String representativeAccount) {
        this(walletId, account, representativeAccount, null);
    }
    
    public RequestAccountRepresentativeSet(String walletId, String account, String representativeAccount, String workSolution) {
        super("account_representative_set", ResponseBlockHash.class);
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
