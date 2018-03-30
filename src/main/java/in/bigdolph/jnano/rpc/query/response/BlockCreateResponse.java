package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.model.block.Block;

public class BlockCreateResponse extends RpcResponse {

    @Expose
    @SerializedName("hash")
    private String blockHash;
    
    @Expose
    @SerializedName("block")
    private Block block;
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
    public Block getBlock() {
        return block;
    }
    
    public String getBlockJson() {
        return block.getJsonRepresentation();
    }
    
}
