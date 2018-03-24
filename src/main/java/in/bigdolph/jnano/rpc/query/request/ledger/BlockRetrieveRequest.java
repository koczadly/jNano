package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.specific.BlockRetrieveResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class BlockRetrieveRequest extends RPCRequest<BlockRetrieveResponse> {
    
    @Expose
    @SerializedName("hash")
    private String blockHash;
    
    
    public BlockRetrieveRequest(String blockHash) {
        super("block", BlockRetrieveResponse.class);
        this.blockHash = blockHash;
    }
    
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
}
