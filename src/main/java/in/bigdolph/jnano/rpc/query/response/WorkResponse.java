package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorkResponse extends RpcResponse {

    @Expose
    @SerializedName("work")
    private String workSolution;
    
    
    
    public String getWorkSolution() {
        return workSolution;
    }
    
}
