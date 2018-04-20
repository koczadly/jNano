package uk.oczadly.karl.jnano.rpc.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.BlocksResponse;

public class BlocksRetrieveRequest extends RpcRequest<BlocksResponse> {
    
    @Expose
    @SerializedName("hashes")
    private String[] blockHashes;
    
    
    public BlocksRetrieveRequest(String... blockHashes) {
        super("blocks", BlocksResponse.class);
        this.blockHashes = blockHashes;
    }
    
    
    
    public String[] getBlockHashes() {
        return blockHashes;
    }
    
}
