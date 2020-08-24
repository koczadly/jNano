package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This response class contains the number of blocks in the local ledger.
 */
public class ResponseBlockCount extends RpcResponse {
    
    @Expose @SerializedName("count")
    private long processed;
    
    @Expose @SerializedName("unchecked")
    private long unchecked;
    
    @Expose @SerializedName("cemented")
    private long cemented;
    
    
    /**
     * @return the number of processed blocks
     */
    public long getProcessedBlocks() {
        return processed;
    }
    
    /**
     * @return the number of unprocessed blocks (awaiting synchronization)
     */
    public long getUncheckedBlocks() {
        return unchecked;
    }
    
    /**
     * @return how many blocks are cemented
     */
    public long getCemented() {
        return cemented;
    }
    
}
