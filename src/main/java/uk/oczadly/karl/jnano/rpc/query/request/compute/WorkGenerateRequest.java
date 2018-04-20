package uk.oczadly.karl.jnano.rpc.query.request.compute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.WorkResponse;

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
