/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket.topic.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.HexData;

public class TopicMessageStoppedElection {
    
    @Expose @SerializedName("hash")
    private HexData hash;
    
    
    // TODO: add javadoc
    public HexData getHash() {
        return hash;
    }
    
}
