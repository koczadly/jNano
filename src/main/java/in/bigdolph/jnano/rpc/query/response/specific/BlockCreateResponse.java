package in.bigdolph.jnano.rpc.query.response.specific;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.model.block.Block;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.math.BigInteger;

public class BlockCreateResponse extends RPCResponse {

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
