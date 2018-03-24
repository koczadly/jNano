package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.specific.BlocksInfoResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class BlocksInfoRequest extends RPCRequest<BlocksInfoResponse> {
    
    @Expose
    @SerializedName("hashes")
    private String[] blockHashes;
    
    @Expose
    @SerializedName("pending")
    private boolean fetchPending = true;
    
    @Expose
    @SerializedName("source")
    private boolean fetchSource = true;
    
    
    public BlocksInfoRequest(String... blockHashes) {
        super("blocks_info", BlocksInfoResponse.class);
        this.blockHashes = blockHashes;
    }
    
    
    
    public String[] getBlockHashes() {
        return blockHashes;
    }
    
}
