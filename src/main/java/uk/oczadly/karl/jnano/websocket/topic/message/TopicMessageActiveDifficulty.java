/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket.topic.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;

public class TopicMessageActiveDifficulty {
    
    @Expose @SerializedName("network_minimum")
    private WorkDifficulty networkMinimum;
    
    @Expose @SerializedName("network_current")
    private WorkDifficulty networkCurrent;
    
    @Expose @SerializedName("network_receive_minimum")
    private WorkDifficulty networkReceiveMinimum;
    
    @Expose @SerializedName("network_receive_current")
    private WorkDifficulty networkReceiveCurrent;
    
    @Expose @SerializedName("multiplier")
    private double multiplier;
    
    
    public WorkDifficulty getNetworkMinimum() {
        return networkMinimum;
    }
    
    public WorkDifficulty getNetworkCurrent() {
        return networkCurrent;
    }
    
    public WorkDifficulty getNetworkReceiveMinimum() {
        return networkReceiveMinimum;
    }
    
    public WorkDifficulty getNetworkReceiveCurrent() {
        return networkReceiveCurrent;
    }
    
    public double getMultiplier() {
        return multiplier;
    }
    
}
