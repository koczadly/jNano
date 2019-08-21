package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class ResponseAmount extends RpcResponse {

    @Expose @SerializedName(value = "amount", alternate = {"weight", "available"})
    private BigInteger amount;
    
    
    
    public BigInteger getAmount() {
        return amount;
    }
    
}
