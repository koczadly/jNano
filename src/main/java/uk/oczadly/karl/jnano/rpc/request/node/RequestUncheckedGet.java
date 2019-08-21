package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlock;

public class RequestUncheckedGet extends RpcRequest<ResponseBlock> {
    
    @Expose @SerializedName("json_block")
    private final boolean json = true;
    
    @Expose @SerializedName("hash")
    private String blockHash;
    
    
    public RequestUncheckedGet(String blockHash) {
        super("unchecked_get", ResponseBlock.class);
        this.blockHash = blockHash;
    }
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
}
