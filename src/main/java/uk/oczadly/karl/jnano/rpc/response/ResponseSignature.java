package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.rpc.RpcResponse;

/**
 * This response class contains a block and an accompanying signature.
 */
public class ResponseSignature extends RpcResponse {
    
    @Expose @SerializedName("block")
    private Block block;
    
    @Expose @SerializedName("signature")
    private String signature;
    
    
    /**
     * @return the contents of the block
     */
    public Block getBlock() {
        return block;
    }
    
    /**
     * @return the signature of the block
     */
    public String getSignature() {
        return signature;
    }
    
}
