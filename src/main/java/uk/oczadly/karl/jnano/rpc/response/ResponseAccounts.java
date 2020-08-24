package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import uk.oczadly.karl.jnano.internal.gsonadapters.SingleValueJsonAdapter;

import java.util.Set;

/**
 * This response class contains a set of account addresses.
 */
@JsonAdapter(SingleValueJsonAdapter.class)
public class ResponseAccounts extends RpcResponse {
    
    @Expose
    private Set<String> accounts;
    
    
    /**
     * @return a set of account addresses
     */
    public Set<String> getAccounts() {
        return accounts;
    }
    
}
