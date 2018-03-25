package in.bigdolph.jnano.rpc.query.response.specific;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.math.BigInteger;

public class UnitConversionResponse extends RPCResponse {

    @Expose
    @SerializedName("amount")
    private BigInteger convertedAmount;
    
    
    
    public BigInteger getConvertedAmount() {
        return convertedAmount;
    }
    
}
