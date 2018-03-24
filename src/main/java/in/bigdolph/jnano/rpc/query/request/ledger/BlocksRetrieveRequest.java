package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.specific.BlocksRetrieveResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class BlocksRetrieveRequest extends RPCRequest<BlocksRetrieveResponse> {
    
    @Expose
    @SerializedName("hashes")
    private String[] blockHashes;
    
    
    public BlocksRetrieveRequest(String... blockHashes) {
        super("blocks", BlocksRetrieveResponse.class);
        this.blockHashes = blockHashes;
    }
    
    
    
    public String[] getBlockHashes() {
        return blockHashes;
    }
    
}
