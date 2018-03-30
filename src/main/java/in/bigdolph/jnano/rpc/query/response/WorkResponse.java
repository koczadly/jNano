package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

public class WorkResponse extends RPCResponse {

    @Expose
    @SerializedName("work")
    private String workSolution;
    
    
    
    public String getWorkSolution() {
        return workSolution;
    }
    
}
