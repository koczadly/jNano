package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.rpc.RpcResponse;

/**
 * This response class contains a block and a timestamp.
 */
public class ResponseBlock extends RpcResponse {
    
    @Expose @SerializedName("modified_timestamp")
    private Integer timestamp;
    
    @Expose @SerializedName("contents")
    private Block block;
    
    
    /**
     * @return the UNIX timestamp when this block was last modified locally
     */
    public Integer getModifiedTimestamp() {
        return timestamp;
    }
    
    /**
     * @return the block's contents
     */
    public Block getBlock() {
        return block;
    }
    
}
