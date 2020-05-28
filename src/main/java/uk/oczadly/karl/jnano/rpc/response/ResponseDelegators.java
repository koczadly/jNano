package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.Map;

/**
 * This response class contains information a representatives delegators.
 */
public class ResponseDelegators extends RpcResponse {
    
    @Expose @SerializedName("delegators")
    private Map<String, BigInteger> delegators;
    
    
    /**
     * Map follows the structure {@code delegator address -> delegated amount (in RAW)}.
     *
     * @return a map of delegators
     */
    public Map<String, BigInteger> getDelegators() {
        return delegators;
    }
    
    /**
     * @param accountAddress the account's address
     * @return the balance delegated to the representative
     */
    public BigInteger getDelegatedBalance(String accountAddress) {
        return delegators.get(accountAddress.toLowerCase());
    }
    
}
