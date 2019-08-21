package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

public class RequestWalletRepresentativeSet extends RpcRequest<ResponseSuccessful> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    @Expose @SerializedName("representative")
    private String representativeAccount;
    
    
    @Expose @SerializedName("update_existing_accounts")
    private Boolean updateExistingAccounts;
    
    
    public RequestWalletRepresentativeSet(String walletId, String representativeAccount) {
        this(walletId, representativeAccount, null);
    }
    
    public RequestWalletRepresentativeSet(String walletId, String representativeAccount, Boolean updateExistingAccounts) {
        super("wallet_representative_set", ResponseSuccessful.class);
        this.walletId = walletId;
        this.representativeAccount = representativeAccount;
        this.updateExistingAccounts = updateExistingAccounts;
    }
    
    
    public String getWalletId() {
        return walletId;
    }
    
    public String getRepresentativeAccount() {
        return representativeAccount;
    }
    
    public Boolean getUpdateExistingAccounts() {
        return updateExistingAccounts;
    }
    
}
