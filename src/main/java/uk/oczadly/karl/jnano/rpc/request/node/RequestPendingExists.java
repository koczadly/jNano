package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseExists;

public class RequestPendingExists extends RpcRequest<ResponseExists> {
    
    @Expose @SerializedName("hash")
    private String blockHash;
    
    
    public RequestPendingExists(String blockHash) {
        super("pending_exists", ResponseExists.class);
        this.blockHash = blockHash;
    }
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
}
