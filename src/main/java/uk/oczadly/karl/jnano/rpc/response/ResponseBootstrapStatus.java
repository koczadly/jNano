package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This response class contains debug information about the current bootstrap attempt.
 */
public class ResponseBootstrapStatus extends RpcResponse {
    
    @Expose @SerializedName("bootstrap_threads")
    private int bootstrapThreads;
    
    @Expose @SerializedName("running_attempts_count")
    private int runningAttemptsCount;
    
    @Expose @SerializedName("total_attempts_count")
    private int totalAttemptsCount;
    
    @Expose @SerializedName("connections")
    private Connections connections;
    
    @Expose @SerializedName("connections")
    private List<Attempt> attempts;
    
    
    public int getBootstrapThreads() {
        return bootstrapThreads;
    }
    
    public int getRunningAttemptsCount() {
        return runningAttemptsCount;
    }
    
    public int getTotalAttemptsCount() {
        return totalAttemptsCount;
    }
    
    public Connections getConnections() {
        return connections;
    }
    
    public List<Attempt> getAttempts() {
        return attempts;
    }
    
    
    
    public static class Connections {
        @Expose @SerializedName("clients") private int clients;
        @Expose @SerializedName("connections") private int connections;
        @Expose @SerializedName("idle") private int idle;
        @Expose @SerializedName("target_connections") private int targetConnections;
        @Expose @SerializedName("pulls") private int pulls;
    
        public int getClients() {
            return clients;
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
    
        public int getPulls() {
            return pulls;
        }
    }
    
    public static class Attempt {
        @Expose @SerializedName("id") private String id;
        @Expose @SerializedName("mode") private BootstrapMode mode;
        @Expose @SerializedName("started") private boolean started;
        @Expose @SerializedName("pulling") private int pulling;
        @Expose @SerializedName("total_blocks") private long totalBlocks;
        @Expose @SerializedName("frontier_pulls") private int frontierPulls;
        @Expose @SerializedName("frontiers_received") private boolean frontiersReceived;
        @Expose @SerializedName("frontiers_confirmed") private boolean frontiersConfirmed;
        @Expose @SerializedName("frontiers_confirmation_pending") private boolean frontiersConfirmationPending;
        @Expose @SerializedName("requeued_pulls") private int requeuedPulls;
        @Expose @SerializedName("lazy_blocks") private int lazyBlocks;
        @Expose @SerializedName("lazy_state_backlog") private int lazyStateBacklog;
        @Expose @SerializedName("lazy_balances") private int lazyBalances;
        @Expose @SerializedName("lazy_destinations") private int lazyDestinations;
        @Expose @SerializedName("lazy_undefined_links") private int lazyUndefinedLinks;
        @Expose @SerializedName("lazy_pulls") private int lazyPulls;
        @Expose @SerializedName("lazy_keys") private int lazyKeys;
        @Expose @SerializedName("lazy_key_1") private String lazyKey1;
        @Expose @SerializedName("duration") private long duration;
    
        public String getId() {
            return id;
        }
    
        public BootstrapMode getMode() {
            return mode;
        }
    
        public boolean isStarted() {
            return started;
        }
    
        public int getPulling() {
            return pulling;
        }
    
        public long getTotalBlocks() {
            return totalBlocks;
        }
    
        public int getFrontierPulls() {
            return frontierPulls;
        }
    
        public boolean isFrontiersReceived() {
            return frontiersReceived;
        }
    
        public boolean isFrontiersConfirmed() {
            return frontiersConfirmed;
        }
    
        public boolean isFrontiersConfirmationPending() {
            return frontiersConfirmationPending;
        }
    
        public int getRequeuedPulls() {
            return requeuedPulls;
        }
    
        public int getLazyBlocks() {
            return lazyBlocks;
        }
    
        public int getLazyStateBacklog() {
            return lazyStateBacklog;
        }
    
        public int getLazyBalances() {
            return lazyBalances;
        }
    
        public int getLazyDestinations() {
            return lazyDestinations;
        }
    
        public int getLazyUndefinedLinks() {
            return lazyUndefinedLinks;
        }
    
        public int getLazyPulls() {
            return lazyPulls;
        }
    
        public int getLazyKeys() {
            return lazyKeys;
        }
    
        public String getLazyKey1() {
            return lazyKey1;
        }
    
        public long getDuration() {
            return duration;
        }
    }
    
    public enum BootstrapMode {
        @SerializedName("lazy")         LAZY,
        @SerializedName("wallet_lazy")  WALLET_LAZY,
        @SerializedName("legacy")       LEGACY;
        
    }
    
}
