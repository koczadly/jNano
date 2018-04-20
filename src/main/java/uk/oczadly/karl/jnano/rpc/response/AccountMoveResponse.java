package uk.oczadly.karl.jnano.rpc.response;

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
