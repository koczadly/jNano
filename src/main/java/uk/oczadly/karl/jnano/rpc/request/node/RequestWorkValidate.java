package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWorkValidation;

public class RequestWorkValidate extends RpcRequest<ResponseWorkValidation> {
    
    @Expose @SerializedName("work")
    private String workSolution;
    
    @Expose @SerializedName("hash")
    private String blockHash;
    
    
    @Expose @SerializedName("difficulty")
    private String difficulty;
    
    @Expose @SerializedName("multiplier")
    private Double multiplier;
    
    
    public RequestWorkValidate(String workSolution, String blockHash) {
        this(workSolution, blockHash, null, null);
    }
    
    public RequestWorkValidate(String workSolution, String blockHas, String difficulty, Double multiplier) {
        super("work_validate", ResponseWorkValidation.class);
        this.workSolution = workSolution;
        this.blockHash = blockHash;
        this.difficulty = difficulty;
        this.multiplier = multiplier;
    }
    
    
    public String getWorkSolution() {
        return workSolution;
    }
    
    public String getBlockHash() {
        return blockHash;
    }
    
    public String getDifficulty() {
        return difficulty;
    }
    
    public Double getMultiplier() {
        return multiplier;
    }
    
}
