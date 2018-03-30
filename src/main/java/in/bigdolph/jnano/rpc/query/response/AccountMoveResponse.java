package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountMoveResponse extends RpcResponse {

    @Expose
    @SerializedName("moved")
    private int accountsMoved;
    
    
    
    public int getAccountsMoved() {
        return accountsMoved;
    }
    
}
