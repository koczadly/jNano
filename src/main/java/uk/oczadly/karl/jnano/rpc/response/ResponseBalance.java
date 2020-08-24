package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

/**
 * This response class contains both pocketed and pending balance amounts.
 */
public class ResponseBalance extends RpcResponse {
    
    @Expose @SerializedName("balance")
    private BigInteger pocketed;
    
    @Expose @SerializedName("pending")
    private BigInteger pending;
    
    
    /**
     * @return the total balance (pocketed)
     */
    public BigInteger getPocketed() {
        return pocketed;
    }
    
    /**
     * @return the total balance from pending blocks
     */
    public BigInteger getPending() {
        return pending;
    }
    
    /**
     * @return the total balance of the account
     */
    public BigInteger getTotal() {
        return pending.add(pocketed);
    }
    
}
