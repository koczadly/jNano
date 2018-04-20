package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BlockCountResponse extends RpcResponse {

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
