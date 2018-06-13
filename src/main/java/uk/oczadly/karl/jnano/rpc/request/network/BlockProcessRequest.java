package uk.oczadly.karl.jnano.rpc.request.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.BlockHashResponse;

public class BlockProcessRequest extends RpcRequest<BlockHashResponse> {
    
    @Expose
    @SerializedName("block")
    private String blockJson;
    
    @Expose
    @SerializedName("force")
    private boolean force;
    
    
    public BlockProcessRequest(Block block, boolean force) {
        this(block.getJsonRepresentation(), force);
    }
    
    public BlockProcessRequest(String blockJson, boolean force) {
        super("process", BlockHashResponse.class);
        this.blockJson = blockJson;
        this.force = force;
    }
    
    
    public String getBlockJson() {
        return blockJson;
    }
    
    public boolean shouldForce() {
        return force;
    }
    
}
