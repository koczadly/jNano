package uk.oczadly.karl.jnano.websocket.topic.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopicMessageBootstrap {
    
    @Expose @SerializedName("reason")
    private String reason;
    
    @Expose @SerializedName("id")
    private String id;
    
    @Expose @SerializedName("mode")
    private String mode;
    
    @Expose @SerializedName("total_blocks")
    private Integer totalBlocks;
    
    @Expose @SerializedName("duration")
    private Integer duration;
    
    
    public String getReason() {
        return reason;
    }
    
    public String getId() {
        return id;
    }
    
    public String getMode() {
        return mode;
    }
    
    public Integer getTotalBlocks() {
        return totalBlocks;
    }
    
    public Integer getDuration() {
        return duration;
    }
}
