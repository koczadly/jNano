package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHash;

public class RequestProcess extends RpcRequest<ResponseBlockHash> {
    
    @Expose @SerializedName("json_block")
    private final boolean json = false;
    
    
    @Expose @SerializedName("block")
    private String blockJson;
    
    @Expose @SerializedName("force")
    private boolean force;
    
    @Expose @SerializedName("subtype")
    private String subtype;
    
    
    public RequestProcess(Block block, Boolean force) {
        this(block.getJsonString(), force, null);
    }
    
    public RequestProcess(String blockJson, Boolean force, String subtype) {
        super("process", ResponseBlockHash.class);
        this.blockJson = blockJson;
        this.force = force;
        this.subtype = subtype;
    }
    
    
    public String getBlockJson() {
        return blockJson;
    }
    
    public boolean shouldForce() {
        return force;
    }
    
    public String getSubtype() {
        return subtype;
    }
    
}
