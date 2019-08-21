package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseWorkValidation extends RpcResponse {
    
    @Expose @SerializedName("valid")
    private boolean isValid;
    
    @Expose @SerializedName("difficulty")
    private String difficulty;
    
    @Expose @SerializedName("multiplier")
    private double multiplier;
    
    
    public boolean isValid() {
        return isValid;
    }
    
    public String getDifficulty() {
        return difficulty;
    }
    
    public double getMultiplier() {
        return multiplier;
    }
    
}
