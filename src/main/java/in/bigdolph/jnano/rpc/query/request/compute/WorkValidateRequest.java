package in.bigdolph.jnano.rpc.query.request.compute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.generic.ValidatedResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class WorkValidateRequest extends RPCRequest<ValidatedResponse> {
    
    @Expose
    @SerializedName("work")
    private String workSolution;
    
    @Expose
    @SerializedName("hash")
    private String blockHash;
    
    
    public WorkValidateRequest(String workSolution, String blockHash) {
        super("work_validate", ValidatedResponse.class);
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
