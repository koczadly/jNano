/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.block.Block;

import java.util.Collection;
import java.util.Map;

/**
 * This response class contains a map of block hashes and their contents.
 */
public class ResponseBlocksMap extends RpcResponse {
    
    @Expose @SerializedName("blocks")
    private Map<HexData, Block> blocks;
    
    
    /**
     * Map follows the structure {@code hash -> block}.
     *
     * @return a list of blocks
     */
    public Map<HexData, Block> getBlocks() {
        return blocks;
    }
    
    /**
     * @return a collection of blocks
     */
    public Collection<Block> getBlocksList() {
        return blocks.values();
    }
    
    /**
     * @param blockHash the block's hash
     * @return the block's contents
     */
    public Block getBlock(String blockHash) {
        return getBlock(new HexData(blockHash));
    }
    
    /**
     * @param blockHash the block's hash
     * @return the block's contents
     */
    public Block getBlock(HexData blockHash) {
        return blocks.get(blockHash);
    }
    
}
