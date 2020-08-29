/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;

/**
 * This response class contains a block hash and it's contents.
 */
public class ResponseBlockCreate extends RpcResponse {
    
    @Expose @SerializedName("hash")
    private String blockHash;
    
    @Expose @SerializedName("difficulty")
    private WorkDifficulty difficulty;
    
    @Expose @SerializedName("block")
    private Block block;
    
    
    /**
     * @return the block's hash
     */
    public String getBlockHash() {
        return blockHash;
    }
    
    /**
     * @return the absolute difficulty of the work field
     */
    public WorkDifficulty getDifficulty() {
        return difficulty;
    }
    
    /**
     * @return the block's contents
     */
    public Block getBlock() {
        return block;
    }
    
}
