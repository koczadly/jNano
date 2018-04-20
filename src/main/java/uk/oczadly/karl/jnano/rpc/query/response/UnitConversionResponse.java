package uk.oczadly.karl.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class UnitConversionResponse extends RpcResponse {

    @Expose
    @SerializedName("amount")
    private BigInteger convertedAmount;
    
    
    
    public BigInteger getConvertedAmount() {
        return convertedAmount;
    }
    
}
