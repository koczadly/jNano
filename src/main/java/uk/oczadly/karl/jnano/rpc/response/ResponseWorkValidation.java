package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcResponse;

/**
 * This response class contains validation results of a given work solution.
 */
public class ResponseWorkValidation extends RpcResponse {
    
    @Expose @SerializedName("valid")
    private boolean isValid;
    
    @Expose @SerializedName("difficulty")
    private String difficulty;
    
    @Expose @SerializedName("multiplier")
    private double multiplier;
    
    
    /**
     * @return true if the given work is valid
     */
    public boolean isValid() {
        return isValid;
    }
    
    /**
     * @return the absolute difficulty value
     */
    public String getDifficulty() {
        return difficulty;
    }
    
    /**
     * @return the difficulity multiplier of this work
     */
    public double getMultiplier() {
        return multiplier;
    }
    
}
