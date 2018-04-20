package uk.oczadly.karl.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class AccountWeightResponse extends RpcResponse {

    @Expose
    @SerializedName("weight")
    private BigInteger weight;
    
    
    
    public BigInteger getWeight() {
        return weight;
    }
    
}
