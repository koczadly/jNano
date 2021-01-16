/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket.topic.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.util.List;

public class TopicMessageWork {
    
    @Expose @SerializedName("success")
    private boolean success;
    
    @Expose @SerializedName("reason")
    private Reason reason;
    
    @Expose @SerializedName("duration")
    private int duration;
    
    @Expose @SerializedName("request")
    private Request request;
    
    @Expose @SerializedName("result")
    private Result result;
    
    @Expose @SerializedName("bad_peers")
    private List<String> badPeers;
    
    
    /**
     * Returns true if the work generation was successful.
     * @return true if the work generation was successful
     */
    public boolean isSuccess() {
        return success;
    }
    
    /**
     * Returns the outcome of the work generation attempt.
     * @return the outcome of the work generation attempt
     */
    public Reason getReason() {
        return isSuccess() ? Reason.SUCCESS : reason;
    }
    
    /**
     * Returns the duration of the work generation process, in milliseconds.
     * @return the duration of the work generation in millis
     */
    public int getDuration() {
        return duration;
    }
    
    /**
     * Returns the work generation request data.
     * @return the work generation request
     */
    public Request getRequest() {
        return request;
    }
    
    /**
     * Returns the work generation result.
     * @return the work generation result
     */
    public Result getResult() {
        return result;
    }
    
    /**
     * Returns a list of IP/ports of "bad" peers. These are work generation peers which were unresponsive, or
     * returned an incorrect work solution.
     * @return a list of bad peers
     */
    public List<String> getBadPeers() {
        return badPeers;
    }
    
    public enum Reason {
        SUCCESS, CANCELLED, FAILURE
    }
    
    public static class Request {
        @Expose @SerializedName("hash")
        private String hash;
        
        @Expose @SerializedName("difficulty")
        private WorkDifficulty difficulty;
        
        @Expose @SerializedName("multiplier")
        private double multiplier;
        
        @Expose @SerializedName("version")
        private String version;
    
    
        /**
         * The root hash of the work generation request.
         * @return the root hash
         */
        public String getHash() {
            return hash;
        }
    
        /**
         * Returns the requested minimum difficulty threshold.
         * @return the requested minimum difficulty
         */
        public WorkDifficulty getDifficulty() {
            return difficulty;
        }
    
        /**
         * Returns the requested difficulty multiplier.
         * @return the difficulty multiplier
         */
        public double getMultiplier() {
            return multiplier;
        }
    
        /**
         * Returns the requested work version identifier.
         * @return the requested work version
         */
        public String getVersion() {
            return version;
        }
    }
    
    public static class Result {
        @Expose @SerializedName("source")
        private String source;
        
        @Expose @SerializedName("work")
        private WorkSolution work;
        
        @Expose @SerializedName("difficulty")
        private WorkDifficulty difficulty;
        
        @Expose @SerializedName("multiplier")
        private double multiplier;
    
    
        /**
         * Returns the IP/port of the node which generated the successful work value.
         * @return the IP/port of the work peer
         */
        public String getPeerAddress() {
            return source;
        }

        /**
         * Returns whether the work was generated on the local node.
         * @return true if the work was generated on the local node
         */
        public boolean wasGeneratedLocally() {
            return getPeerAddress().equals("local");
        }
    
        /**
         * Returns the generated work solution.
         * @return the generated work
         */
        public WorkSolution getWork() {
            return work;
        }
    
        /**
         * Returns the difficulty of the generated work.
         * @return the difficulty of the work
         */
        public WorkDifficulty getDifficulty() {
            return difficulty;
        }
    
        /**
         * Returns the difficulty multiplier of the generated work.
         * @return the difficulty multiplier of the work
         */
        public double getMultiplier() {
            return multiplier;
        }
    }
    
}
