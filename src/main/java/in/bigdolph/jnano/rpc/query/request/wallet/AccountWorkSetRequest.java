package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.RpcResponse;

public class AccountWorkSetRequest extends RpcRequest<RpcResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    @Expose
    @SerializedName("account")
    private String account;
    
    @Expose
    @SerializedName("work")
    private String workSolution;
    
    
    public AccountWorkSetRequest(String walletId, String account, String workSolution) {
        super("work_set", RpcResponse.class);
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
