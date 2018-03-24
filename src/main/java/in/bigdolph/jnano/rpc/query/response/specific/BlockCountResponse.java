package in.bigdolph.jnano.rpc.query.response.specific;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

public class BlockCountResponse extends RPCResponse {

    @Expose
    @SerializedName("count")
    private long processed;
    
    @Expose
    @SerializedName("unchecked")
    private long unchecked;
    
    
    
    public long getProcessedBlocks() {
        return processed;
    }
    
    public long getUncheckedBlocks() {
        return unchecked;
    }
    
    public long getTotalBlocks() {
        return processed + unchecked;
    }
    
}
