package uk.oczadly.karl.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.BlockHashResponse;

public class BlockProcessRequest extends RpcRequest<BlockHashResponse> {
    
    @Expose
    @SerializedName("block")
    private String blockJson;
    
    
    public BlockProcessRequest(Block block) {
        this(block.getJsonRepresentation());
    }
    
    public BlockProcessRequest(String blockJson) {
        super("process", BlockHashResponse.class);
        this.blockJson = blockJson;
    }
    
    
    public String getBlockJson() {
        return blockJson;
    }
    
}
