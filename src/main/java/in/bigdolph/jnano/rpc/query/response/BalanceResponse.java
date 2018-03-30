package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class BalanceResponse extends RpcResponse {

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
