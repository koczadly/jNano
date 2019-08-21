package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseWork extends RpcResponse {
    
    @Expose @SerializedName("work")
    private String workSolution;
    
    @Expose @SerializedName("difficulty")
    private String difficulty;
    
    @Expose @SerializedName("multiplier")
    private double multiplier;
    
    
    
    public String getWorkSolution() {
        return workSolution;
    }
    
    public String getDifficulty() {
        return difficulty;
    }
    
    public double getMultiplier() {
        return multiplier;
    }
    
}
