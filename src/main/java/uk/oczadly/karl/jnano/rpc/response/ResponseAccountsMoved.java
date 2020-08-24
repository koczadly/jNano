package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import uk.oczadly.karl.jnano.internal.gsonadapters.SingleValueJsonAdapter;

/**
 * This response class contains a single number of accounts that have been moved.
 */
@JsonAdapter(SingleValueJsonAdapter.class)
public class ResponseAccountsMoved extends RpcResponse {
    
    @Expose
    private int accountsMoved;
    
    
    /**
     * @return the number of accounts moved
     */
    public int getAccountsMoved() {
        return accountsMoved;
    }
    
}
