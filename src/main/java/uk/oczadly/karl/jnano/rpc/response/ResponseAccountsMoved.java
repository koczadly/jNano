package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This response class contains a single number of accounts that have been moved.
 */
public class ResponseAccountsMoved extends RpcResponse {

    @Expose @SerializedName("moved")
    private int accountsMoved;
    
    
    /**
     * @return the number of accounts moved
     */
    public int getAccountsMoved() {
        return accountsMoved;
    }
    
}
