package in.bigdolph.jnano.rpc.query.response.specific;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.math.BigInteger;

public class AccountWeightResponse extends RPCResponse {

    @Expose
    @SerializedName("weight")
    private BigInteger weight;
    
    
    
    public BigInteger getWeight() {
        return weight;
    }
    
}
