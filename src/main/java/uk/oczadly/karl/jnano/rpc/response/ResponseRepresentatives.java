package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseRepresentatives extends RpcResponse {
    
    @Expose @SerializedName("representatives")
    private LinkedHashMap<String, BigInteger> representatives;
    
    
    public Map<String, BigInteger> getRepresentatives() {
        return representatives;
    }
    
    public BigInteger getVotingWeight(String accountAddress) {
        return representatives.get(accountAddress.toLowerCase());
    }
    
}
