package uk.oczadly.karl.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountRemoveResponse extends RpcResponse {

    @Expose
    @SerializedName("removed")
    private int accountsRemoved;
    
    
    
    public int getAccountsRemoved() {
        return accountsRemoved;
    }
    
}
