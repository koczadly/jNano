package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.AccountAddress;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This response class contains a set of online representatives who have recently voted.
 */
public class ResponseOnlineRepresentatives extends RpcResponse {
    
    @Expose @SerializedName("representatives")
    private LinkedHashMap<AccountAddress, RepInfo> representatives;
    
    
    /**
     * Map follows the structure {@code rep address -> information}.
     *
     * @return a map of representatives
     */
    public Map<AccountAddress, RepInfo> getRepresentatives() {
        return representatives;
    }
    
    /**
     * @param accountAddress a representative's account address
     * @return details associated with the specified representative, or null if not present in the response
     */
    public RepInfo getRepresentative(AccountAddress accountAddress) {
        return representatives.get(accountAddress);
    }
    
    
    public static class RepInfo {
        @Expose @SerializedName("weight")
        private BigInteger weight;
        
        
        /**
         * @return the delegated voting weight of this representative
         */
        public BigInteger getWeight() {
            return weight;
        }
    }
    
}
