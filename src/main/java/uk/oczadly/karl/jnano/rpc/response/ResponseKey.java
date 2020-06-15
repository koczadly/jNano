package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import uk.oczadly.karl.jnano.internal.gsonadapters.SingleValueJsonAdapter;

/**
 * This response class contains a single String representing a key, either public or private.
 */
@JsonAdapter(SingleValueJsonAdapter.class)
public class ResponseKey extends RpcResponse {
    
    @Expose
    private String key;
    
    
    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }
    
}
