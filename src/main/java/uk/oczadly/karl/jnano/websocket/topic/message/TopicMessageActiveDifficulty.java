package uk.oczadly.karl.jnano.websocket.topic.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;

public class TopicMessageActiveDifficulty {
    
    @Expose @SerializedName("network_minimum")
    private WorkDifficulty networkMinimum;
    
    @Expose @SerializedName("network_current")
    private WorkDifficulty networkCurrent;
    
    @Expose @SerializedName("multiplier")
    private double multiplier;
    
    
    public WorkDifficulty getNetworkMinimum() {
        return networkMinimum;
    }
    
    public WorkDifficulty getNetworkCurrent() {
        return networkCurrent;
    }
    
    public double getMultiplier() {
        return multiplier;
    }
    
}
