package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;

public class ResponseBlockCreate extends RpcResponse {

    @Expose
    @SerializedName("hash")
    private String blockHash;
    
    @Expose
    @SerializedName("block")
    private Block block;
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
    public Block getBlock() {
        return block;
    }
    
}
