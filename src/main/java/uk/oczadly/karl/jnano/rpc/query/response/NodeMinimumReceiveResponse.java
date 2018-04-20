package uk.oczadly.karl.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class NodeMinimumReceiveResponse extends RpcResponse {

    @Expose
    @SerializedName("amount")
    private BigInteger minimumAmount;
    
    
    public BigInteger getMinimumAmount() {
        return minimumAmount;
    }
    
}
