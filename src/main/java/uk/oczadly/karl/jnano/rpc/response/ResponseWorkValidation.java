package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.WorkDifficulty;

/**
 * This response class contains validation results of a given work solution.
 */
public class ResponseWorkValidation extends RpcResponse {
    
    @Expose @SerializedName("valid")
    private Boolean isValidDifficulty;
    
    @Expose @SerializedName("valid_all")
    private boolean isValidAll;
    
    @Expose @SerializedName("valid_receive")
    private boolean isValidReceive;
    
    @Expose @SerializedName("difficulty")
    private WorkDifficulty difficulty;
    
    @Expose @SerializedName("multiplier")
    private double multiplier;
    
    
    /**
     * @return whether the work is valid for the given difficulty, or matches the requirements for all block types
     * @deprecated changed in V21, use appropriate replacement getter method
     */
    @Deprecated(forRemoval = true)
    public boolean isValid() {
        return isValidDifficulty != null ? isValidDifficulty : isValidAll;
    }
    
    /**
     * @return true if the given work is valid for the given difficulty, or null if no comparative difficulty was
     * sent with the request.
     */
    public Boolean isValidDifficulty() {
        return isValidDifficulty;
    }
    
    /**
     * @return true if the work is valid for all block types
     */
    public boolean isValidAll() {
        return isValidAll;
    }
    
    /**
     * @return true if the work is valid for <i>receive</i> block types
     */
    public boolean isValidReceive() {
        return isValidReceive;
    }
    
    /**
     * @return the absolute difficulty value
     */
    public WorkDifficulty getDifficulty() {
        return difficulty;
    }
    
    /**
     * @return the difficulity multiplier of this work
     */
    public double getMultiplier() {
        return multiplier;
    }
    
}
