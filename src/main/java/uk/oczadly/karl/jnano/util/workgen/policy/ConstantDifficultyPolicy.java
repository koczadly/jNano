/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen.policy;

import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;

/**
 * This work difficulty policy returns a single given difficulty for all given block types.
 */
public final class ConstantDifficultyPolicy implements ConstantWorkDifficultyPolicy {
    
    private final WorkDifficulty difficulty;
    
    /**
     * Constructs a new policy instance from a constant value.
     *
     * @param difficulty the work difficulty
     */
    public ConstantDifficultyPolicy(WorkDifficulty difficulty) {
        this.difficulty = difficulty;
    }
    
    
    @Override
    public WorkDifficulty forBlock(Block block) {
        return difficulty;
    }
    
    public WorkDifficulty forAny() {
        return difficulty;
    }
    
    /**
     * Returns a fixed value of {@code 1.0}.
     * @return {@code 1.0}
     */
    @Override
    public double multiplier() {
        return 1;
    }
    
}
