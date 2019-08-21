package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHash;

public class RequestBlockHash extends RpcRequest<ResponseBlockHash> {
    
    @Expose @SerializedName("json_block")
    private final boolean json = false;
    
    @Expose @SerializedName("block")
    private String block;
    
    
    public RequestBlockHash(Block block) {
        this(block.getJsonString());
    }
    
    public RequestBlockHash(String blockJson) {
        super("block_hash", ResponseBlockHash.class);
        this.block = blockJson;
    }
    
    
    public String getBlockJson() {
        return block;
    }
    
}
