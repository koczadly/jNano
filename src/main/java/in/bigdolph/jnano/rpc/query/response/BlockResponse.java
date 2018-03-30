package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.model.block.Block;

public class BlockResponse extends RpcResponse {
    
    @Expose
    @SerializedName("contents")
    private Block block;
    
    
    
    public Block getBlock() {
        return block;
    }
    
}
