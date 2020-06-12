package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.AccountAddress;

import java.math.BigInteger;
import java.util.Map;

/**
 * This response class contains information a representatives delegators.
 */
public class ResponseDelegators extends RpcResponse {
    
    @Expose @SerializedName("delegators")
    private Map<AccountAddress, BigInteger> delegators;
    
    
    /**
     * Map follows the structure {@code delegator address -> delegated amount (in RAW)}.
     *
     * @return a map of delegators
     */
    public Map<AccountAddress, BigInteger> getDelegators() {
        return delegators;
    }
    
    /**
     * @param accountAddress the account's address
     * @return the balance delegated to the representative
     */
    public BigInteger getDelegatedBalance(AccountAddress accountAddress) {
        return delegators.get(accountAddress);
    }
    
}
