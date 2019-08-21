package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockInfo;

public class RequestBlockInfo extends RpcRequest<ResponseBlockInfo> {
    
    @Expose @SerializedName("json_block")
    private final boolean json = true;
    
    @Expose @SerializedName("hash")
    private String hash;
    
    
    public RequestBlockInfo(String blockHash) {
        super("block_info", ResponseBlockInfo.class);
        this.hash = blockHash;
    }
    
    
    public String getBlockHash() {
        return hash;
    }
    
}
