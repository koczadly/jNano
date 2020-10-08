/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAmount;

import java.util.List;

/**
 * This response class contains historical information about confirmations.
 */
public class ResponseConfirmationHistory extends RpcResponse {
    
    @Expose @SerializedName("confirmation_stats")
    private ConfirmationStats stats;
    
    @Expose @SerializedName("confirmations")
    private List<Confirmation> confirmations;
    
    
    /**
     * @return a set of statistics associated with confirmations
     */
    public ConfirmationStats getStats() {
        return stats;
    }
    
    /**
     * @return a historical list of confirmations
     */
    public List<Confirmation> getConfirmations() {
        return confirmations;
    }
    
    
    public static class ConfirmationStats {
        @Expose @SerializedName("count")
        private int count;
        
        @Expose @SerializedName("average")
        private int average;
        
        
        /**
         * @return the number of confirmations
         */
        public int getCount() {
            return count;
        }
        
        /**
         * @return the average confirmation duration time
         */
        public int getAverage() {
            return average;
        }
    }
    
    public static class Confirmation {
        @Expose @SerializedName("hash")
        private HexData hash;
        
        @Expose @SerializedName("duration")
        private int duration;
        
        @Expose @SerializedName("time")
        private int time;
        
        @Expose @SerializedName("tally")
        private NanoAmount tally;
        
        @Expose @SerializedName("request_count")
        private int requestCount;
        
        
        /**
         * @return the block's hash
         */
        public HexData getHash() {
            return hash;
        }
        
        /**
         * @return the elapsed duration of the confirmation
         */
        public int getDuration() {
            return duration;
        }
        
        /**
         * @return the starting UNIX timestamp of the confirmation
         */
        public int getTime() {
            return time;
        }
        
        /**
         * @return the total voted weight of this block
         */
        public NanoAmount getTally() {
            return tally;
        }
        
        /**
         * @return the number of requests made for this block
         */
        public int getRequestCount() {
            return requestCount;
        }
    }
    
}
