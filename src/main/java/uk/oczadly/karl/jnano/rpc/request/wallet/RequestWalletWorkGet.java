package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWalletWork;

public class RequestWalletWorkGet extends RpcRequest<ResponseWalletWork> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    
    public RequestWalletWorkGet(String walletId) {
        super("wallet_work_get", ResponseWalletWork.class);
        this.walletId = walletId;
    }
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
