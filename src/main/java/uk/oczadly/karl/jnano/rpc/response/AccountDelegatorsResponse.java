package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.Map;

public class AccountDelegatorsResponse extends RpcResponse {
    
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
