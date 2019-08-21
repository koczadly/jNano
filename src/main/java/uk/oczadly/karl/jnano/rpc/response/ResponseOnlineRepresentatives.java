package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseOnlineRepresentatives extends RpcResponse {
    
    @Expose @SerializedName("representatives")
    private LinkedHashMap<String, RepInfo> representatives;
    
    
    public Map<String, RepInfo> getRepresentatives() {
        return representatives;
    }
    
    public RepInfo getRepresentative(String accountAddress) {
        return representatives.get(accountAddress.toLowerCase());
    }
    
    
    
    public static class RepInfo {
        @Expose @SerializedName("weight")
        private BigInteger weight;
    
    
        public BigInteger getWeight() {
            return weight;
        }
    }
    
}
