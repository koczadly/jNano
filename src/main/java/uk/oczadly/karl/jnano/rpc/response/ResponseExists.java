package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import uk.oczadly.karl.jnano.internal.gsonadapters.SingleValueJsonAdapter;

/**
 * This response class contains a boolean which represents whether something exists.
 */
@JsonAdapter(SingleValueJsonAdapter.class)
public class ResponseExists extends RpcResponse {
    
    @Expose
    private boolean exists;
    
    
    /**
     * @return if the entity exists
     */
    public boolean doesExist() {
        return exists;
    }
    
}
