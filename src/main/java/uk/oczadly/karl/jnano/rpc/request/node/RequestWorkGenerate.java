package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWork;

public class RequestWorkGenerate extends RpcRequest<ResponseWork> {
    
    @Expose @SerializedName("hash")
    private String blockHash;
    
    @Expose @SerializedName("use_peers")
    private Boolean usePeers;
    
    @Expose @SerializedName("difficulty")
    private String difficulty;
    
    @Expose @SerializedName("multiplier")
    private Double multiplier;
    
    
    public RequestWorkGenerate(String blockHash) {
        this(blockHash, null, null, null);
    }
    
    public RequestWorkGenerate(String blockHash, Boolean usePeers, String difficulty, Double multiplier) {
        super("work_generate", ResponseWork.class);
        this.blockHash = blockHash;
        this.usePeers = usePeers;
        this.difficulty = difficulty;
        this.multiplier = multiplier;
    }
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
    public boolean getUsePeers() {
        return usePeers;
    }
    
    public String getDifficulty() {
        return difficulty;
    }
    
    public Double getMultiplier() {
        return multiplier;
    }
    
}
