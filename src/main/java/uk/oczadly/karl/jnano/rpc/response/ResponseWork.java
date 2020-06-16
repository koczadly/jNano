package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

/**
 * This response class contains a computed work solution.
 */
public class ResponseWork extends RpcResponse {
    
    @Expose @SerializedName("work")
    private WorkSolution workSolution;
    
    @Expose @SerializedName("difficulty")
    private WorkDifficulty difficulty;
    
    @Expose @SerializedName("multiplier")
    private double multiplier;
    
    
    /**
     * @return the generated work solution
     */
    public WorkSolution getWorkSolution() {
        return workSolution;
    }
    
    /**
     * @return the absolute value of this difficulty
     */
    public WorkDifficulty getDifficulty() {
        return difficulty;
    }
    
    /**
     * @return the multiplier of this difficulty
     */
    public double getMultiplier() {
        return multiplier;
    }
    
}
