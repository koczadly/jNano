package in.bigdolph.jnano.rpc.query.response.generic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.math.BigInteger;

public class BalanceResponse extends RPCResponse {

    @Expose
    @SerializedName("balance")
    private BigInteger pocketed;
    
    @Expose
    @SerializedName("pending")
    private BigInteger pending;
    
    
    
    public BigInteger getPocketed() {
        return pocketed;
    }
    
    public BigInteger getPending() {
        return pending;
    }
    
    public BigInteger getTotal() {
        return pending.add(pocketed);
    }
    
}
