package in.bigdolph.jnano.rpc.query.request.compute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

public class WorkCancelRequest extends RPCRequest<RPCResponse> {
    
    @Expose
    @SerializedName("hash")
    private String blockHash;
    
    
    public WorkCancelRequest(String blockHash) {
        super("work_cancel", RPCResponse.class);
        this.blockHash = blockHash;
    }
    
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
}
