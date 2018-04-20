package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

public class FetchRepresentativesResponse extends RpcResponse {
    
    @Expose
    @SerializedName("representatives")
    private LinkedHashMap<String, BigInteger> representatives;
    
    
    
    public LinkedHashMap<String, BigInteger> getRepresentatives() {
        return representatives;
    }
    
    public LinkedHashMap<String, BigInteger> getRepresentatives(BigInteger minimumWeight) {
        LinkedHashMap<String, BigInteger> newMap = new LinkedHashMap<>();
        for(Map.Entry<String, BigInteger> rep : representatives.entrySet()) {
            if(rep.getValue().compareTo(minimumWeight) >= 0) newMap.put(rep.getKey(), rep.getValue());
        }
        return newMap;
    }
    
    public BigInteger getVotingWeight(String accountAddress) {
        return this.representatives.get(accountAddress.toLowerCase());
    }
    
}
