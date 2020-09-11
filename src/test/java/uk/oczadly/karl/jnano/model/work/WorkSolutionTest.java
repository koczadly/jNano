/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.work;

import org.junit.Test;
import uk.oczadly.karl.jnano.TestConstants;
import uk.oczadly.karl.jnano.model.block.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class WorkSolutionTest {

    @Test
    public void testDiffCalculation() {
        WorkSolution solution = new WorkSolution("6277aa854f897e6f");
        assertEquals(new WorkDifficulty("fffffff4bc8f0cbf"),
                solution.calculateDifficulty("133D48F43EC826CF0B66C78B4B3DDF0D8E57550B0F6119186DB4CB1B5D8ACC35"));
    }
    
    @Test
    public void testRootHash() {
        // Previous
        ChangeBlock changeBlock = TestConstants.randChangeBlock();
        assertEquals(changeBlock.getPreviousBlockHash(), WorkSolution.getRoot(changeBlock));
    
        // Previous
        StateBlock stateBlock = TestConstants.randStateBlock().build();
        assertEquals(stateBlock.getPreviousBlockHash(), WorkSolution.getRoot(stateBlock));
    
        // Account
        stateBlock = TestConstants.randStateBlock().setPreviousBlockHash(null).build();
        assertEquals(stateBlock.getAccount().toPublicKey(), WorkSolution.getRoot(stateBlock));
    
        // Account
        OpenBlock openBlock = TestConstants.randOpenBlock();
        assertEquals(openBlock.getAccount().toPublicKey(), WorkSolution.getRoot(openBlock));
        
        // Error
        Block invalidBlock = new Block("test") {
            @Override
            public BlockIntent getIntent() {
                return null;
            }
            
            @Override
            protected byte[][] generateHashables() {
                return new byte[0][];
            }
        };
        assertThrows(IllegalArgumentException.class,() -> WorkSolution.getRoot(invalidBlock));
    }
    
}