package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseWalletChangeSeed extends RpcResponse {
    
    @Expose @SerializedName("last_restored_account")
    private String lastAccount;
    
    @Expose @SerializedName("restored_count")
    private int restoredCount;
    
    
    public String getLastAccount() {
        return lastAccount;
    }
    
    public int getRestoredCount() {
        return restoredCount;
    }
    
}
