package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.AccountAddress;

import java.util.Map;

/**
 * This response class contains a set of pre-computed work for a wallet.
 */
public class ResponseWalletWork extends RpcResponse {
    
    @Expose @SerializedName("works")
    private Map<AccountAddress, String> work;
    
    
    /**
     * Map follows the structure {@code account address -> work}.
     *
     * @return a map of pre-computed work
     */
    public Map<AccountAddress, String> getWork() {
        return work;
    }
    
    /**
     * @param accountAddress a local account's address
     * @return a pre-computed work solution, or null if no work has been pre-computed
     */
    public String getWork(AccountAddress accountAddress) {
        return this.work.get(accountAddress);
    }
    
}
