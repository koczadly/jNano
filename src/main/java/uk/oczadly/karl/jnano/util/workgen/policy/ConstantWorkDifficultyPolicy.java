/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen.policy;

import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;

/**
 * Represents a constant work difficulty policy, removing the checked exception declarations.
 */
public interface ConstantWorkDifficultyPolicy extends WorkDifficultyPolicy {
    
    /**
     * {@inheritDoc}
     */
    WorkDifficulty forBlock(Block block);
    
    /**
     * {@inheritDoc}
     */
    WorkDifficulty forAny();
    
    /**
     * {@inheritDoc}
     */
    default double multiplier() {
        return 1;
    }

}
