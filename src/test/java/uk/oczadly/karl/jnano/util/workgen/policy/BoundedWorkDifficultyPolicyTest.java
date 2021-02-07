/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen.policy;

import org.junit.Test;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;

import static org.junit.Assert.assertEquals;

/**
 * @author Karl Oczadly
 */
public class BoundedWorkDifficultyPolicyTest {
    
    @Test
    public void testBounds() throws Exception {
        TestPolicy policy = new TestPolicy();
        WorkDifficultyPolicy bounded = new BoundedWorkDifficultyPolicy(policy, 2, 5);
    
        // Too low
        policy.multiplier = 1;
        assertEquals(2d, bounded.multiplier(), 1e-15);
    
        // Too high
        policy.multiplier = 7;
        assertEquals(5d, bounded.multiplier(), 1e-15);
    
        // In range
        policy.multiplier = 3.5;
        assertEquals(3.5, bounded.multiplier(), 1e-15);
    }

    
    
    static class TestPolicy implements WorkDifficultyPolicy {
        double multiplier;
        
        @Override
        public WorkDifficulty forBlock(Block block) throws DifficultyRetrievalException {
            return null;
        }
    
        @Override
        public WorkDifficulty forAny() throws DifficultyRetrievalException {
            return null;
        }
    
        @Override
        public double multiplier() throws DifficultyRetrievalException {
            return multiplier;
        }
    }

}