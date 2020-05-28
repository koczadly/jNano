package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

/**
 * This response class contains information about the current network active difficulty.
 */
public class ResponseActiveDifficulty extends RpcResponse {
    
    @Expose @SerializedName("network_minimum")
    private String networkMinimum;
    
    @Expose @SerializedName("network_current")
    private String networkCurrent;
    
    @Expose @SerializedName("multiplier")
    private BigDecimal multiplier;
    
    @Expose @SerializedName("difficulty_trend")
    private List<BigDecimal> difficultyTrend;
    
    
    /**
     * @return the minimum work value required
     */
    public String getNetworkMinimum() {
        return networkMinimum;
    }
    
    /**
     * @return the current average work value
     */
    public String getNetworkCurrent() {
        return networkCurrent;
    }
    
    /**
     * @return the current average multiplier
     */
    public BigDecimal getMultiplier() {
        return multiplier;
    }
    
    /**
     * @return a list of historical difficulty trends
     */
    public List<BigDecimal> getDifficultyTrend() {
        return difficultyTrend;
    }
    
}
