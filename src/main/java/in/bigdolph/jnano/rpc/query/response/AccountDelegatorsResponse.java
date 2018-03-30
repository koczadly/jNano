package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.math.BigInteger;
import java.util.Map;

public class AccountDelegatorsResponse extends RPCResponse {
    
    @Expose
    @SerializedName("delegators")
    private Map<String, BigInteger> delegators;
    
    
    
    public Map<String, BigInteger> getDelegators() {
        return delegators;
    }
    
    public BigInteger getBalance(String accountAddress) {
        return this.delegators.get(accountAddress.toLowerCase());
    }
    
}
