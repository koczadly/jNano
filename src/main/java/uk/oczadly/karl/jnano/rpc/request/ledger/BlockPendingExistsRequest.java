package uk.oczadly.karl.jnano.rpc.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ExistsResponse;

public class BlockPendingExistsRequest extends RpcRequest<ExistsResponse> {
    
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
