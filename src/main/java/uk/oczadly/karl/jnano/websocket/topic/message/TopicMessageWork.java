/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket.topic.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.net.URI;

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
    
    
    public boolean isSuccess() {
        return success;
    }
    
    public Reason getReason() {
        return reason;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public Request getRequest() {
        return request;
    }
    
    public Result getResult() {
        return result;
    }
    
    
    
    public enum Reason {
        @SerializedName("cancelled")    CANCELLED,
        @SerializedName("failure")      FAILURE,
        @SerializedName("")             SUCCESS;
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
        
    
        public String getHash() {
            return hash;
        }
    
        public WorkDifficulty getDifficulty() {
            return difficulty;
        }
    
        public double getMultiplier() {
            return multiplier;
        }
    
        public String getVersion() {
            return version;
        }
    }
    
    public static class Result {
        @Expose @SerializedName("source")
        private URI source;
        
        @Expose @SerializedName("work")
        private WorkSolution work;
        
        @Expose @SerializedName("difficulty")
        private WorkDifficulty difficulty;
        
        @Expose @SerializedName("multiplier")
        private double multiplier;
    
    
        public URI getSource() {
            return source;
        }
    
        public WorkSolution getWork() {
            return work;
        }
    
        public WorkDifficulty getDifficulty() {
            return difficulty;
        }
    
        public double getMultiplier() {
            return multiplier;
        }
    }
    
}
