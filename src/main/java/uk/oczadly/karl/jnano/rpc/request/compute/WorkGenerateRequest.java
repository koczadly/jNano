package uk.oczadly.karl.jnano.rpc.request.compute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.WorkResponse;

public class WorkGenerateRequest extends RpcRequest<WorkResponse> {
    
    @Expose
    @SerializedName("hash")
    private String blockHash;
    
    @Expose
    @SerializedName("use_peers")
    private boolean usePeers;
    
    
    public WorkGenerateRequest(String blockHash) {
        this(blockHash, false);
    }
    
    public WorkGenerateRequest(String blockHash, boolean usePeers) {
        super("work_generate", WorkResponse.class);
        this.blockHash = blockHash;
        this.usePeers = usePeers;
    }
    
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
    public boolean getUsePeers() {
        return usePeers;
    }
}
