package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseExists;

public class RequestWalletContains extends RpcRequest<ResponseExists> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    @Expose @SerializedName("account")
    private String account;
    
    
    public RequestWalletContains(String walletId, String account) {
        super("wallet_contains", ResponseExists.class);
        this.walletId = walletId;
        this.account = account;
    }
    
    
    public String getWalletId() {
        return walletId;
    }
    
    public String getAccount() {
        return account;
    }
    
}
