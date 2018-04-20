package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BlockHashResponse extends RpcResponse {

    @Expose
    @SerializedName(value="block", alternate={"hash"})
    private String blockHash;
    
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
}
