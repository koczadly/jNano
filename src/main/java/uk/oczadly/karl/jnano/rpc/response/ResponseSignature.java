package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;

public class ResponseSignature extends RpcResponse {
    
    @Expose @SerializedName("block")
    private Block block;
    
    @Expose @SerializedName("signature")
    private String signature;
    
    
    public Block getBlock() {
        return block;
    }
    
    public String getSignature() {
        return signature;
    }
    
}
