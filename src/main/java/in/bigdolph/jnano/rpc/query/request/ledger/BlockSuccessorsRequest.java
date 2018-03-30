package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.BlockHashResponse;

public class BlockSuccessorsRequest extends RPCRequest<BlockHashResponse> {
    
    @Expose
    @SerializedName("hash")
    private String blockHash;
    
    @Expose
    @SerializedName("count")
    private int count;
    
    
    public BlockSuccessorsRequest(String blockHash, int count) {
        super("successors", BlockHashResponse.class);
        this.blockHash = blockHash;
        this.count = count;
    }
    
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
    public int getCount() {
        return count;
    }
    
}
