/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;

import java.math.BigDecimal;
import java.util.List;

/**
 * This response class contains information about the current network active difficulty.
 */
public class ResponseActiveDifficulty extends RpcResponse {
    
    @Expose @SerializedName("network_minimum")
    private WorkDifficulty networkMinimum;
    
    @Expose @SerializedName("network_current")
    private WorkDifficulty networkCurrent;
    
    @Expose @SerializedName("network_receive_minimum")
    private WorkDifficulty networkReceiveMinimum;
    
    @Expose @SerializedName("network_receive_current")
    private WorkDifficulty networkReceiveCurrent;
    
    @Expose @SerializedName("multiplier")
    private BigDecimal multiplier;
    
    @Expose @SerializedName("difficulty_trend")
    private List<BigDecimal> difficultyTrend;
    
    
    /**
     * @return the minimum work value required for any block type
     */
    public WorkDifficulty getNetworkMinimum() {
        return networkMinimum;
    }
    
    /**
     * @return the current average work value for all block types
     */
    public WorkDifficulty getNetworkCurrent() {
        return networkCurrent;
    }
    
    /**
     * @return the minimum work value required for receiving blocks
     */
    public WorkDifficulty getNetworkReceiveMinimum() {
        return networkReceiveMinimum;
    }
    
    /**
     * @return the current average work value for receive blocks
     */
    public WorkDifficulty getNetworkReceiveCurrent() {
        return networkReceiveCurrent;
    }
    
    /**
     * @return the current recommended difficulty multiplier
     */
    public BigDecimal getMultiplier() {
        return multiplier;
    }
    
    /**
     * @return a list of historical difficulty multipliers, or null if not requested
     */
    public List<BigDecimal> getDifficultyTrend() {
        return difficultyTrend;
    }
    
}
