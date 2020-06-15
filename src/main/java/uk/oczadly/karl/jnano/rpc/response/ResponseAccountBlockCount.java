package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import uk.oczadly.karl.jnano.internal.gsonadapters.SingleValueJsonAdapter;

/**
 * This response class contains a single block count.
 */
@JsonAdapter(SingleValueJsonAdapter.class)
public class ResponseAccountBlockCount extends RpcResponse {
    
    @Expose
    private long blockCount;
    
    
    /**
     * @return the number of blocks
     */
    public long getBlockCount() {
        return blockCount;
    }
    
}
