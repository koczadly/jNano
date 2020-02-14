package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcResponse;

/**
 * This response class contains debug information about the current bootstrap attempt.
 */
public class ResponseBootstrapStatus extends RpcResponse {

    @Expose @SerializedName("count")
    private int clients;
    
    @Expose @SerializedName("pulls")
    private long pulls;
    
    @Expose @SerializedName("pulling")
    private int pulling;
    
    @Expose @SerializedName("connections")
    private int connections;
    
    @Expose @SerializedName("idle")
    private int idle;
    
    @Expose @SerializedName("target_connections")
    private int targetConnections;
    
    @Expose @SerializedName("total_blocks")
    private long totalBlocks;
    
    @Expose @SerializedName("lazy_mode")
    private boolean lazyMode;
    
    @Expose @SerializedName("lazy_blocks")
    private long lazyBlocks;
    
    @Expose @SerializedName("lazy_state_unknown")
    private int LazyStateUnknown;
    
    @Expose @SerializedName("lazy_balances")
    private int lazyBalances;
    
    @Expose @SerializedName("lazy_pulls")
    private int lazyPulls;
    
    @Expose @SerializedName("lazy_stopped")
    private int lazyStopped;
    
    @Expose @SerializedName("lazy_keys")
    private int lazyKeys;
    
    @Expose @SerializedName("lazy_key_1")
    private String LazyKey1;
    
    
    public int getClients() {
        return clients;
    }
    
    public long getPulls() {
        return pulls;
    }
    
    public int getPulling() {
        return pulling;
    }
    
    public int getConnections() {
        return connections;
    }
    
    public int getIdle() {
        return idle;
    }
    
    public int getTargetConnections() {
        return targetConnections;
    }
    
    public long getTotalBlocks() {
        return totalBlocks;
    }
    
    public boolean isLazyMode() {
        return lazyMode;
    }
    
    public long getLazyBlocks() {
        return lazyBlocks;
    }
    
    public int getLazyStateUnknown() {
        return LazyStateUnknown;
    }
    
    public int getLazyBalances() {
        return lazyBalances;
    }
    
    public int getLazyPulls() {
        return lazyPulls;
    }
    
    public int getLazyStopped() {
        return lazyStopped;
    }
    
    public int getLazyKeys() {
        return lazyKeys;
    }
    
    public String getLazyKey1() {
        return LazyKey1;
    }
    
}
