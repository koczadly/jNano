/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket.topic.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.HexData;

public class TopicMessageBootstrap {
    
    @Expose @SerializedName("reason")
    private Reason reason;
    
    @Expose @SerializedName("id")
    private HexData id;
    
    @Expose @SerializedName("mode")
    private Mode mode;
    
    @Expose @SerializedName("total_blocks")
    private Integer totalBlocks;
    
    @Expose @SerializedName("duration")
    private Integer duration;
    
    
    /**
     * Returns the reason of the bootstrap status update.
     * @return the reason of the bootstrap status update
     */
    public Reason getReason() {
        return reason;
    }
    
    /**
     * Returns the ID of the bootstrap connection.
     * @return the ID of the bootstrap connection
     */
    public HexData getId() {
        return id;
    }
    
    /**
     * Returns the bootstrapping mode.
     * @return the bootstrapping mode
     */
    public Mode getMode() {
        return mode;
    }
    
    /**
     * Returns the number of blocks synced from this connection.
     * <p>This method will throw an {@link IllegalStateException} if the reason is not {@link Reason#EXITED EXITED}.</p>
     * @return the number of blocks synced from this connection
     */
    public int getTotalBlocks() {
        if (reason != Reason.EXITED)
            throw new IllegalStateException("Total blocks value only available for exited bootstraps.");
        return totalBlocks;
    }
    
    /**
     * Returns the duration of this connection in seconds.
     * <p>This method will throw an {@link IllegalStateException} if the reason is not {@link Reason#EXITED EXITED}.</p>
     * @return the duration of this connection in seconds
     */
    public int getDuration() {
        if (reason != Reason.EXITED)
            throw new IllegalStateException("Duration value only available for exited bootstraps.");
        return duration;
    }
    
    
    public enum Reason {
        STARTED, EXITED
    }
    
    public enum Mode {
        LEGACY, LAZY, WALLET_LAZY;
    }
}
