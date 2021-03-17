/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.blockproducer;

import uk.oczadly.karl.jnano.model.block.Block;

/**
 * Represents a {@link Block} and equivalent {@link AccountState} at the time of the block.
 */
public class BlockAndState {
    
    private final Block block;
    private final AccountState state;
    
    public BlockAndState(Block block, AccountState state) {
        this.block = block;
        this.state = state;
    }
    
    
    /**
     * @return the block
     */
    public final Block getBlock() {
        return block;
    }
    
    /**
     * @return the account state <em>after</em> the block has been processed
     */
    public final AccountState getState() {
        return state;
    }
    
}
