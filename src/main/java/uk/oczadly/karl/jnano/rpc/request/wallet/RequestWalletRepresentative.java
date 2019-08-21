package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccount;

public class RequestWalletRepresentative extends RpcRequest<ResponseAccount> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    
    public RequestWalletRepresentative(String walletId) {
        super("wallet_representative", ResponseAccount.class);
        this.walletId = walletId;
    }
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
