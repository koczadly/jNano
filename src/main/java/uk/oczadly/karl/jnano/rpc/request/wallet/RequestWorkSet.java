package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

public class RequestWorkSet extends RpcRequest<ResponseSuccessful> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    @Expose @SerializedName("account")
    private String account;
    
    @Expose @SerializedName("work")
    private String workSolution;
    
    
    public RequestWorkSet(String walletId, String account, String workSolution) {
        super("work_set", ResponseSuccessful.class);
        this.walletId = walletId;
        this.account = account;
        this.workSolution = workSolution;
    }
    
    
    public String getWalletId() {
        return walletId;
    }
    
    public String getAccount() {
        return account;
    }
    
    public String getWorkSolution() {
        return workSolution;
    }
    
}
