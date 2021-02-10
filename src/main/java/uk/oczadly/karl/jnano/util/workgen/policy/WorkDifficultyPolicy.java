/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen.policy;

import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.util.NetworkConstants;

/**
 * Represents a work difficulty policy.
 *
 * @see NetworkConstants#NANO
 */
public interface WorkDifficultyPolicy {
    
    /**
     * Returns the minimum work difficulty threshold for a given block.
     *
     * <p>The difficulty value returned by this method should not factor in the recommended {@link #multiplier()} value,
     * and should simply return the base difficulty. If you want to multiply the value, call
     * {@link WorkDifficulty#multiply(double)} on the returned object.</p>
     *
     * @param block the block to retrieve the difficulty value for
     * @return the minimum difficulty threshold
     * @throws DifficultyRetrievalException if an exception occurs when trying to retrieve difficulty values
     */
    WorkDifficulty forBlock(Block block) throws DifficultyRetrievalException;
    
    /**
     * Returns the minimum work difficulty threshold for any and all block types.
     *
     * <p>The difficulty value returned by this method should not factor in the recommended {@link #multiplier()} value,
     * and should simply return the base difficulty. If you want to multiply the value, call
     * {@link WorkDifficulty#multiply(double)} on the returned object.</p>
     *
     * @return the minimum difficulty threshold
     * @throws DifficultyRetrievalException if an exception occurs when trying to retrieve difficulty values
     */
    WorkDifficulty forAny() throws DifficultyRetrievalException;
    
    /**
     * Returns the current recommended difficulty multiplier value. This value should not be applied to the difficulty
     * retrieval methods by default, and should be applied after where necessary.
     *
     * <p>For simple non-dynamic implementations, this method should simply return a value of {@code 1.0}.</p>
     *
     * @return the current recommended multiplier
     * @throws DifficultyRetrievalException if an exception occurs when trying to retrieve difficulty values
     */
    default double multiplier() throws DifficultyRetrievalException {
        return 1;
    }

}
