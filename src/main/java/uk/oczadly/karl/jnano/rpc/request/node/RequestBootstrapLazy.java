package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

public class RequestBootstrapLazy extends RpcRequest<ResponseSuccessful> {
    
    @Expose
    @SerializedName("hash")
    private String blockHash;
    
    @Expose
    @SerializedName("force")
    private Boolean force;
    
    
    public RequestBootstrapLazy(String blockHash) {
        this(blockHash, null);
    }
    
    public RequestBootstrapLazy(String blockHash, Boolean force) {
        super("bootstrap_lazy", ResponseSuccessful.class);
        this.blockHash = blockHash;
        this.force = force;
    }
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
    public Boolean getForce() {
        return force;
    }
    
}
