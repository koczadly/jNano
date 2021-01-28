/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.work.generator.policy;

import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.util.NanoConstants;

/**
 * Represents a work difficulty policy.
 *
 * @see NanoConstants#NANO_LIVE_NET
 */
public interface WorkDifficultyPolicy {
    
    /**
     * Returns the minimum work difficulty threshold for a given block.
     *
     * <p>The difficulty value returned by this method will already factor in the {@link #multiplier()} value.</p>
     *
     * @param block the block to retrieve the difficulty value for
     * @return the minimum difficulty threshold
     * @throws DifficultyRetrievalException if an exception occurs when trying to retrieve difficulty values
     */
    WorkDifficulty forBlock(Block block) throws DifficultyRetrievalException;
    
    /**
     * Returns the minimum work difficulty threshold for any and all block types.
     *
     * <p>The difficulty value returned by this method will already factor in the {@link #multiplier()} value.</p>
     *
     * @return the minimum difficulty threshold
     * @throws DifficultyRetrievalException if an exception occurs when trying to retrieve difficulty values
     */
    WorkDifficulty forAny() throws DifficultyRetrievalException;
    
    /**
     * Returns the current recommended difficulty multiplier value.
     *
     * <p>For simple non-dynamic implementations, this method should simply return a value of {@code 1.0}.</p>
     *
     * @return the current recommended multiplier
     * @throws DifficultyRetrievalException if an exception occurs when trying to retrieve difficulty values
     */
    double multiplier() throws DifficultyRetrievalException;

}
