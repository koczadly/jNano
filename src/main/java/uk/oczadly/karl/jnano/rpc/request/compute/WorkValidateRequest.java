package uk.oczadly.karl.jnano.rpc.request.compute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ValidationResponse;

public class WorkValidateRequest extends RpcRequest<ValidationResponse> {
    
    @Expose
    @SerializedName("work")
    private String workSolution;
    
    @Expose
    @SerializedName("hash")
    private String blockHash;
    
    
    public WorkValidateRequest(String workSolution, String blockHash) {
        super("work_validate", ValidationResponse.class);
        this.workSolution = workSolution;
        this.blockHash = blockHash;
    }
    
    
    
    public String getWorkSolution() {
        return workSolution;
    }
    
    public String getBlockHash() {
        return blockHash;
    }
    
}
