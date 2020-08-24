package uk.oczadly.karl.jnano.websocket.topic.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopicMessageStoppedElection {
    
    @Expose @SerializedName("hash")
    private String hash;
    
    
    public String getHash() {
        return hash;
    }
    
}
