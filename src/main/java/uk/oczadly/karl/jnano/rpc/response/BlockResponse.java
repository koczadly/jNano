package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;

public class BlockResponse extends RpcResponse {
    
    @Expose
    @SerializedName("contents")
    private Block block;
    
    
    
    public Block getBlock() {
        return block;
    }
    
}
