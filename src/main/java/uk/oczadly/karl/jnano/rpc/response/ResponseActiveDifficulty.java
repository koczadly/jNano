package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

public class ResponseActiveDifficulty extends RpcResponse {
    
    @Expose @SerializedName("network_minimum")
    private String networkMinimum;
    
    @Expose @SerializedName("network_current")
    private String networkCurrent;
    
    @Expose @SerializedName("multiplier")
    private BigDecimal multiplier;
    
    @Expose @SerializedName("difficulty_trend")
    private List<BigDecimal> difficultyTrend;
    
    
    public String getNetworkMinimum() {
        return networkMinimum;
    }
    
    public String getNetworkCurrent() {
        return networkCurrent;
    }
    
    public BigDecimal getMultiplier() {
        return multiplier;
    }
    
    public List<BigDecimal> getDifficultyTrend() {
        return difficultyTrend;
    }
    
}
