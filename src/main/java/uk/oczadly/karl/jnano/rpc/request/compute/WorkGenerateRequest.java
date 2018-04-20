package uk.oczadly.karl.jnano.rpc.request.compute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.WorkResponse;

public class WorkGenerateRequest extends RpcRequest<WorkResponse> {
    
    @Expose
    @SerializedName("hash")
    private String blockHash;
    
    
    public WorkGenerateRequest(String blockHash) {
        super("work_generate", WorkResponse.class);
        this.blockHash = blockHash;
    }
    
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
}
