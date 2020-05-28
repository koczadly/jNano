package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This response class contains a computed work solution.
 */
public class ResponseWork extends RpcResponse {
    
    @Expose @SerializedName("work")
    private String workSolution;
    
    @Expose @SerializedName("difficulty")
    private String difficulty;
    
    @Expose @SerializedName("multiplier")
    private double multiplier;
    
    
    /**
     * @return the generated work solution
     */
    public String getWorkSolution() {
        return workSolution;
    }
    
    /**
     * @return the absolute value of this difficulty
     */
    public String getDifficulty() {
        return difficulty;
    }
    
    /**
     * @return the multiplier of this difficulty
     */
    public double getMultiplier() {
        return multiplier;
    }
    
}
