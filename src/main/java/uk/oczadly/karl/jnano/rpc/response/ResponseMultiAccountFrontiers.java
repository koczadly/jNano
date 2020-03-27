package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcResponse;

import java.util.Map;

/**
 * This response class contains a set of accounts and their head blocks.
 */
public class ResponseMultiAccountFrontiers extends RpcResponse {
    
    @Expose @SerializedName("frontiers")
    private Map<String, String> frontiers;
    
    
    /**
     * Map follows the structure {@code address -> head block hash}.
     *
     * @return a map of account frontiers
     */
    public Map<String, String> getFrontiers() {
        return frontiers;
    }
    
    /**
     * @param accountAddress an account's address
     * @return the head block hash of the specified account, or null if not present in the response
     */
    public String getFrontierBlockHash(String accountAddress) {
        return this.frontiers.get(accountAddress.toLowerCase());
    }
    
}
