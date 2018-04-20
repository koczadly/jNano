package uk.oczadly.karl.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.BlockHashesResponse;

public class BlockChainRequest extends RpcRequest<BlockHashesResponse> {
    
    @Expose
    @SerializedName("hash")
    private String blockHash;
    
    @Expose
    @SerializedName("count")
    private int count;
    
    
    public BlockChainRequest(String blockHash, int count) {
        super("chain", BlockHashesResponse.class);
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
