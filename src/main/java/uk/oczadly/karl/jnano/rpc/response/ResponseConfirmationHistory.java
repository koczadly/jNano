package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.List;

public class ResponseConfirmationHistory extends RpcResponse {
    
    @Expose @SerializedName("confirmation_stats")
    private ConfirmationStats stats;
    
    @Expose @SerializedName("confirmations")
    private List<Confirmation> confirmations;
    
    
    public ConfirmationStats getStats() {
        return stats;
    }
    
    public List<Confirmation> getConfirmations() {
        return confirmations;
    }
    
    
    
    public static class ConfirmationStats {
        @Expose @SerializedName("count")
        private int count;
        
        @Expose @SerializedName("average")
        private int average;
        
    
        public int getCount() {
            return count;
        }
    
        public int getAverage() {
            return average;
        }
    }
    
    public static class Confirmation {
        @Expose @SerializedName("hash")
        private String hash;
    
        @Expose @SerializedName("duration")
        private int duration;
    
        @Expose @SerializedName("time")
        private int time;
    
        @Expose @SerializedName("tally")
        private BigInteger tally;
    
    
        public String getHash() {
            return hash;
        }
    
        public int getDuration() {
            return duration;
        }
    
        public int getTime() {
            return time;
        }
    
        public BigInteger getTally() {
            return tally;
        }
    }
    
}
