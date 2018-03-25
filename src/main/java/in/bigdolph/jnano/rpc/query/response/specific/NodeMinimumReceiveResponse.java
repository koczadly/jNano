package in.bigdolph.jnano.rpc.query.response.specific;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.math.BigInteger;

public class NodeMinimumReceiveResponse extends RPCResponse {

    @Expose
    @SerializedName("amount")
    private BigInteger minimumAmount;
    
    
    public BigInteger getMinimumAmount() {
        return minimumAmount;
    }
    
}
