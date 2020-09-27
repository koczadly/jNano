/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.gsonadapters.InstantAdapter;
import uk.oczadly.karl.jnano.model.block.Block;

import java.time.Instant;

/**
 * This response class contains a block and a timestamp.
 */
public class ResponseBlock extends RpcResponse {
    
    @Expose @SerializedName("modified_timestamp") @JsonAdapter(InstantAdapter.Seconds.class)
    private Instant timestamp;
    
    @Expose @SerializedName("contents")
    private Block block;
    
    
    /**
     * @return the UNIX timestamp when this block was last modified locally
     */
    public Instant getModifiedTimestamp() {
        return timestamp;
    }
    
    /**
     * @return the block's contents
     */
    public Block getBlock() {
        return block;
    }
    
}
