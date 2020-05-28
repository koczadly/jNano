package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This response class contains a set of representatives.
 */
public class ResponseRepresentatives extends RpcResponse {
    
    @Expose @SerializedName("representatives")
    private LinkedHashMap<String, BigInteger> representatives;
    
    
    /**
     * Map follows the structure {@code address -> voting weight}.
     *
     * @return a map of representatives
     */
    public Map<String, BigInteger> getRepresentatives() {
        return representatives;
    }
    
    /**
     * @param accountAddress a representative's address
     * @return the voting weight of the specified representative, or null if not present in the response
     */
    public BigInteger getVotingWeight(String accountAddress) {
        return representatives.get(accountAddress.toLowerCase());
    }
    
}
