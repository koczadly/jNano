package in.bigdolph.jnano.rpc.query.request.compute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.WorkResponse;

public class WorkGenerateRequest extends RPCRequest<WorkResponse> {
    
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
