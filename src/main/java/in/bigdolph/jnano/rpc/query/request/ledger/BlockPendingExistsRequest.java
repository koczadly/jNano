package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.ExistsResponse;

public class BlockPendingExistsRequest extends RPCRequest<ExistsResponse> {
    
    @Expose
    @SerializedName("hash")
    private String blockHash;
    
    
    public BlockPendingExistsRequest(String blockHash) {
        super("pending_exists", ExistsResponse.class);
        this.blockHash = blockHash;
    }
    
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
}
