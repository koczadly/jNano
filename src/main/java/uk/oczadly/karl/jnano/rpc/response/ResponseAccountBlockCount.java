package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseAccountBlockCount extends RpcResponse {

    @Expose @SerializedName("block_count")
    private long blockCount;
    
    
    
    public long getBlockCount() {
        return blockCount;
    }
    
}
