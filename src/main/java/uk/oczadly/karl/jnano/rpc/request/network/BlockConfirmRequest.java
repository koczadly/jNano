package uk.oczadly.karl.jnano.rpc.request.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.BlockResponse;
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;

public class BlockConfirmRequest extends RpcRequest<RpcResponse> {
    
    @Expose
    @SerializedName("hash")
    private String blockHash;
    
    
    public BlockConfirmRequest(String blockHash) {
        super("block_confirm", RpcResponse.class);
        this.blockHash = blockHash;
    }
    
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
}
