package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcResponse;

/**
 * This response class contains a single block count.
 */
public class ResponseAccountBlockCount extends RpcResponse {

    @Expose @SerializedName("block_count")
    private long blockCount;
    
    
    /**
     * @return the number of blocks
     */
    public long getBlockCount() {
        return blockCount;
    }
    
}
