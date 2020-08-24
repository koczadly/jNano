package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import uk.oczadly.karl.jnano.internal.gsonadapters.SingleValueJsonAdapter;

/**
 * This response class contains a single boolean which represents if something is valid.
 */
@JsonAdapter(SingleValueJsonAdapter.class)
public class ResponseValidation extends RpcResponse {
    
    @Expose
    private boolean isValid;
    
    
    /**
     * @return whether the request was valid
     */
    public boolean isValid() {
        return isValid;
    }
    
}
