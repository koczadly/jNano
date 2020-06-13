package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAccount;

import java.util.Map;

/**
 * This response class contains a set of accounts and their head blocks.
 */
public class ResponseMultiAccountFrontiers extends RpcResponse {
    
    @Expose @SerializedName("frontiers")
    private Map<NanoAccount, String> frontiers;
    
    
    /**
     * Map follows the structure {@code address -> head block hash}.
     *
     * @return a map of account frontiers
     */
    public Map<NanoAccount, String> getFrontiers() {
        return frontiers;
    }
    
    /**
     * @param address an account's address
     * @return the head block hash of the specified account, or null if not present in the response
     */
    public String getFrontierBlockHash(NanoAccount address) {
        return this.frontiers.get(address);
    }
    
}
