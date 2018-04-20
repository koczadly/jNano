package uk.oczadly.karl.jnano.rpc.request.compute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;

public class WorkCancelRequest extends RpcRequest<RpcResponse> {
    
    @Expose
    @SerializedName("hash")
    private String blockHash;
    
    
    public WorkCancelRequest(String blockHash) {
        super("work_cancel", RpcResponse.class);
        this.blockHash = blockHash;
    }
    
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
}
