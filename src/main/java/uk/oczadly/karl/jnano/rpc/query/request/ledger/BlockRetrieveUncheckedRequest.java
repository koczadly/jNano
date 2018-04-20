package uk.oczadly.karl.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.BlockResponse;

public class BlockRetrieveUncheckedRequest extends RpcRequest<BlockResponse> {
    
    @Expose
    @SerializedName("hash")
    private String blockHash;
    
    
    public BlockRetrieveUncheckedRequest(String blockHash) {
        super("unchecked_get", BlockResponse.class);
        this.blockHash = blockHash;
    }
    
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
}
