/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.work.generator.policy;

import org.junit.Test;
import uk.oczadly.karl.jnano.TestConstants;
import uk.oczadly.karl.jnano.model.block.StateBlockSubType;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;

import static org.junit.Assert.assertEquals;

/**
 * @author Karl Oczadly
 */
public class ConstantDifficultyPolicyV2Test {

    @Test
    public void test() {
        WorkDifficulty SEND = new WorkDifficulty(2);
        WorkDifficulty RECEIVE = new WorkDifficulty(1);
        WorkDifficulty MAX = SEND;
        ConstantDifficultyPolicyV2 policy = new ConstantDifficultyPolicyV2(SEND, RECEIVE);
    
        // Any
        assertEquals(MAX, policy.forAny());
    
        // Legacy
        assertEquals(MAX, policy.forBlock(TestConstants.randChangeBlock()));
        assertEquals(MAX, policy.forBlock(TestConstants.randOpenBlock()));
        assertEquals(MAX, policy.forBlock(TestConstants.randReceiveBlock()));
        assertEquals(MAX, policy.forBlock(TestConstants.randSendBlock()));
        
        // State
        assertEquals(SEND, policy.forBlock(TestConstants.randStateBlock(StateBlockSubType.SEND)));
        assertEquals(SEND, policy.forBlock(TestConstants.randStateBlock(StateBlockSubType.CHANGE)));
        assertEquals(RECEIVE, policy.forBlock(TestConstants.randStateBlock(StateBlockSubType.RECEIVE)));
        assertEquals(RECEIVE, policy.forBlock(TestConstants.randStateBlock(StateBlockSubType.OPEN)));
        assertEquals(RECEIVE, policy.forBlock(TestConstants.randStateBlock(StateBlockSubType.EPOCH)));
    }


}