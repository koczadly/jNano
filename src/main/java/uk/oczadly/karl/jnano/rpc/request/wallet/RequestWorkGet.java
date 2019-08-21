package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWork;

public class RequestWorkGet extends RpcRequest<ResponseWork> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    @Expose @SerializedName("account")
    private String account;
    
    
    public RequestWorkGet(String walletId, String account) {
        super("work_get", ResponseWork.class);
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
