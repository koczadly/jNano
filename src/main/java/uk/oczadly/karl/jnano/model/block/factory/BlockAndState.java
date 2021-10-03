/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block.factory;

import uk.oczadly.karl.jnano.model.block.Block;

/**
 * Represents a single {@link Block} and equivalent {@link AccountState} at the time of the block.
 *
 * @param <B> the block type
 */
public class BlockAndState<B extends Block> {
    
    private final B block;
    private final AccountState state;

    /**
     * @param block the block
     * @param state the state of the account at the point in time of the provided block
     */
    public BlockAndState(B block, AccountState state) {
        if (block == null) throw new IllegalArgumentException("Block cannot be null.");
        if (state == null) throw new IllegalArgumentException("State cannot be null.");
        this.block = block;
        this.state = state;
    }
    
    
    /**
     * @return the block
     */
    public final B getBlock() {
        return block;
    }
    
    /**
     * @return the account state at the time of the block
     */
    public final AccountState getState() {
        return state;
    }
    
}
