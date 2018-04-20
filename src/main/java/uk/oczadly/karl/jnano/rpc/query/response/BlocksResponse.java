package uk.oczadly.karl.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;

import java.util.Map;

public class BlocksResponse extends RpcResponse {
    
    @Expose
    @SerializedName("blocks")
    private Map<String, Block> blocks;
    
    
    /** Hash -> Block */
    public Map<String, Block> getBlocks() {
        return blocks;
    }
    
    public Block getBlock(String blockHash) {
        return this.blocks.get(blockHash.toUpperCase());
    }
    
}
