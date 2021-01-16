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
    
    
    /**
     * Returns the minimum required network difficulty for all block types (the base difficulty).
     * @return the minimum required network difficulty for all block types
     */
    public WorkDifficulty getNetworkMinimum() {
        return networkMinimum;
    }
    
    /**
     * Returns the recommended network difficulty for all block types (the base difficulty), based on current network
     * load.
     * @return the current recommended network difficulty for all block types
     */
    public WorkDifficulty getNetworkCurrent() {
        return networkCurrent;
    }
    
    /**
     * Returns the minimum required network difficulty for receive blocks.
     * @return the minimum required network difficulty for receive blocks
     */
    public WorkDifficulty getNetworkReceiveMinimum() {
        return networkReceiveMinimum;
    }
    
    /**
     * Returns the recommended network difficulty for receive blocks, based on current network load.
     * @return the current recommended network difficulty for receive blocks
     */
    public WorkDifficulty getNetworkReceiveCurrent() {
        return networkReceiveCurrent;
    }
    
    /**
     * Returns the recommended difficulty multiplier, based on current network load.
     * @return the current recommended difficulty multiplier
     */
    public double getMultiplier() {
        return multiplier;
    }
    
}
