/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen.policy;

import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;

/**
 * Wraps a {@link WorkDifficultyPolicy} and bounds the recommended multiplier to the defined range. Ideal for use
 * with dynamic difficulty policy providers such as {@link NodeWorkDifficultyPolicy}.
 */
public class BoundedWorkDifficultyPolicy implements WorkDifficultyPolicy {

    private final WorkDifficultyPolicy policy;
    private final double min, max;
    
    /**
     * Constructs a BoundedWorkDifficultyPolicy with an upper limit.
     * @param policy the work difficulty policy to retrieve from
     * @param max the maximum multiplier value
     */
    public BoundedWorkDifficultyPolicy(WorkDifficultyPolicy policy, double max) {
        this(policy, 0, max);
    }
    
    /**
     * Constructs a BoundedWorkDifficultyPolicy with an upper limit.
     * @param policy the work difficulty policy to retrieve from
     * @param min    the minimum multiplier value
     * @param max    the maximum multiplier value
     */
    public BoundedWorkDifficultyPolicy(WorkDifficultyPolicy policy, double min, double max) {
        if (policy == null)
            throw new IllegalArgumentException("Policy cannot be null.");
        if (min < 0 || max < 0)
            throw new IllegalArgumentException("Min and max values must be zero or positive.");
        if (min > max)
            throw new IllegalArgumentException("Min value must be smaller than max.");
        
        this.policy = policy;
        this.min = min;
        this.max = max;
    }
    
    
    /**
     * @return the underlying policy provider
     */
    public WorkDifficultyPolicy getPolicy() {
        return policy;
    }
    
    /**
     * @return the minimum (lower) bounded multiplier value
     */
    public double getMinimumMultiplier() {
        return min;
    }
    
    /**
     * @return the maximum (upper) bounded multiplier value
     */
    public double getMaximumMultiplier() {
        return max;
    }
    
    
    @Override
    public WorkDifficulty forBlock(Block block) throws DifficultyRetrievalException {
        return policy.forBlock(block);
    }
    
    @Override
    public WorkDifficulty forAny() throws DifficultyRetrievalException {
        return policy.forAny();
    }
    
    @Override
    public double multiplier() throws DifficultyRetrievalException {
        return Math.min(Math.max(policy.multiplier(), min), max);
    }
}
