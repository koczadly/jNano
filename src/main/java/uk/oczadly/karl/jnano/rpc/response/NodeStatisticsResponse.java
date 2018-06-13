package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Set;

public class NodeStatisticsResponse extends RpcResponse {
    
    @Expose
    @SerializedName("created")
    private String timestamp;
    
    @Expose
    @SerializedName("entries")
    private Set<StatEntry> stats;
    
    
    public Set<StatEntry> getStatistics() {
        return stats;
    }
    
    public StatEntry getStatistic(Type type, String detail, Direction dir) {
        for(StatEntry stat : this.getStatistics()) {
            if(stat.getType() == type && stat.getDetail().equalsIgnoreCase(detail) && stat.getDirection() == dir) return stat;
        }
        return null;
    }
    
    
    public static class StatEntry {
        @Expose
        @SerializedName("time")
        private String timestamp;
    
        @Expose
        @SerializedName("type")
        private Type type;
    
        @Expose
        @SerializedName("detail")
        private String detail;
    
        @Expose
        @SerializedName("dir")
        private Direction dir;
    
        @Expose
        @SerializedName("value")
        private long value;
    
    
        public String getTimestamp() {
            return timestamp;
        }
    
        public Type getType() {
            return type;
        }
    
        public String getDetail() {
            return detail;
        }
    
        public Direction getDirection() {
            return dir;
        }
    
        public long getValue() {
            return value;
        }
    }
    
    
    public enum Direction {
        @SerializedName("in")   IN,
        @SerializedName("out")  OUT;
    }
    
    public enum Type {
        @SerializedName("traffic")      TRAFFIC,
        @SerializedName("error")        ERROR,
        @SerializedName("message")      MESSAGE,
        @SerializedName("block")        BLOCK,
        @SerializedName("ledger")       LEDGER,
        @SerializedName("rollback")     ROLLBACK,
        @SerializedName("bootstrap")    BOOTSTRAP,
        @SerializedName("vote")         VOTE,
        @SerializedName("peering")      PEERING;
    }
}
