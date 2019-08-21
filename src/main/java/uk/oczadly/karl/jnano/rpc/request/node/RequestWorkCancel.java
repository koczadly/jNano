package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

public class RequestWorkCancel extends RpcRequest<ResponseSuccessful> {
    
    @Expose @SerializedName("hash")
    private String blockHash;
    
    
    public RequestWorkCancel(String blockHash) {
        super("work_cancel", ResponseSuccessful.class);
        this.blockHash = blockHash;
    }
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
}
