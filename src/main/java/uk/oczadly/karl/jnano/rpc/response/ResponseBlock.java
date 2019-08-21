package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;

public class ResponseBlock extends RpcResponse {
    
    @Expose @SerializedName("modified_timestamp")
    private Integer timestamp;
    
    @Expose @SerializedName("contents")
    private Block block;
    
    
    public Integer getModifiedTimestamp() {
        return timestamp;
    }
    
    public Block getBlock() {
        return block;
    }
    
}
