package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.model.block.Block;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.generic.BlockHashResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class BlockProcessRequest extends RPCRequest<BlockHashResponse> {
    
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
