package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class AvailableSupplyResponse extends RpcResponse {

    @Expose
    @SerializedName("available")
    private BigInteger supply;
    
    
    public BigInteger getSupply() {
        return supply;
    }
    
}
