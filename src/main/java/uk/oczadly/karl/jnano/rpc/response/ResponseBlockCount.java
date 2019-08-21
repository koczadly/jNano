package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseBlockCount extends RpcResponse {

    @Expose @SerializedName("count")
    private long processed;
    
    @Expose @SerializedName("unchecked")
    private long unchecked;
    
    @Expose @SerializedName("cemented")
    private long cemented;
    
    
    
    public long getProcessedBlocks() {
        return processed;
    }
    
    public long getUncheckedBlocks() {
        return unchecked;
    }
    
    public long getCemented() {
        return cemented;
    }
    
}
