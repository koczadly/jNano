package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;

public class WalletSetRepresentativeRequest extends RpcRequest<RpcResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    @Expose
    @SerializedName("representative")
    private String representativeAccount;
    
    
    public WalletSetRepresentativeRequest(String walletId, String representativeAccount) {
        super("wallet_representative_set", RpcResponse.class);
        this.walletId = walletId;
        this.representativeAccount = representativeAccount;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
    public String getRepresentativeAccount() {
        return representativeAccount;
    }
    
}
