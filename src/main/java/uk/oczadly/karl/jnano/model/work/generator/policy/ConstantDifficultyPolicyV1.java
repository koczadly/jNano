/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.work.generator.policy;

import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;

/**
 * This work difficulty policy returns a single given difficulty for all given block types.
 */
public final class ConstantDifficultyPolicyV1 implements ConstantWorkDifficultyPolicy {
    
    private final WorkDifficulty difficulty;
    
    /**
     * Constructs a new policy instance from a constant value.
     *
     * @param difficulty the work difficulty
     */
    public ConstantDifficultyPolicyV1(WorkDifficulty difficulty) {
        this.difficulty = difficulty;
    }
    
    
    @Override
    public WorkDifficulty forBlock(Block block) {
        return difficulty;
    }
    
    public WorkDifficulty forAny() {
        return difficulty;
    }
    
    @Override
    public double multiplier() {
        return 1;
    }
    
}
