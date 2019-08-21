package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;

import java.util.Map;

public class ResponseBlocksMap extends RpcResponse {
    
    @Expose @SerializedName("blocks")
    private Map<String, Block> blocks;
    
    
    /** Hash -&gt; Block */
    public Map<String, Block> getBlocks() {
        return blocks;
    }
    
    public Block getBlock(String blockHash) {
        return this.blocks.get(blockHash.toUpperCase());
    }
    
}
