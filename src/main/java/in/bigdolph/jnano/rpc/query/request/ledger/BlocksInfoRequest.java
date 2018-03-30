package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.BlocksInfoResponse;

public class BlocksInfoRequest extends RpcRequest<BlocksInfoResponse> {
    
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
