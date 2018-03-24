package in.bigdolph.jnano.rpc.query.response.specific;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.model.block.Block;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.util.Map;

public class BlocksRetrieveResponse extends RPCResponse {
    
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
