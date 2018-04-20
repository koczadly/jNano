package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.AccountResponse;

public class WalletFetchDefaultRepresentativeRequest extends RpcRequest<AccountResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    
    public WalletFetchDefaultRepresentativeRequest(String walletId) {
        super("wallet_representative", AccountResponse.class);
        this.walletId = walletId;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
