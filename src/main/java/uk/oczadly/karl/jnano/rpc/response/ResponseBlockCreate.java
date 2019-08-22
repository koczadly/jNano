package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;

/**
 * This response class contains a block hash and it's contents.
 */
public class ResponseBlockCreate extends RpcResponse {

    @Expose @SerializedName("hash")
    private String blockHash;
    
    @Expose @SerializedName("block")
    private Block block;
    
    
    /**
     * @return the block's hash
     */
    public String getBlockHash() {
        return blockHash;
    }
    
    /**
     * @return the block's contents
     */
    public Block getBlock() {
        return block;
    }
    
}
