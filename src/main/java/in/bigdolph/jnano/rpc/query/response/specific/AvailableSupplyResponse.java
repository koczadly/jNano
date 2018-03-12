package in.bigdolph.jnano.rpc.query.response.specific;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.math.BigInteger;

public class AvailableSupplyResponse extends RPCResponse {

    @Expose
    @SerializedName("available")
    private BigInteger supply;
    
    
    public BigInteger getSupply() {
        return supply;
    }
    
}
