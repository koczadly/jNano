package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

/**
 * This response class contains a single amount of Nano.
 */
public class ResponseAmount extends RpcResponse {

    @Expose @SerializedName(value = "amount", alternate = {"weight", "available"})
    private BigInteger amount;
    
    
    /**
     * @return the amount of Nano in RAW
     */
    public BigInteger getAmount() {
        return amount;
    }
    
}
