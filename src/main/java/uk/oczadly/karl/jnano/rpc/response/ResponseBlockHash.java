package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import uk.oczadly.karl.jnano.internal.gsonadapters.SingleValueJsonAdapter;

/**
 * This response class contains a single block hash.
 */
@JsonAdapter(SingleValueJsonAdapter.class)
public class ResponseBlockHash extends RpcResponse {
    
    @Expose
    private String blockHash;
    
    
    /**
     * @return the block's hash
     */
    public String getBlockHash() {
        return blockHash;
    }
    
}
