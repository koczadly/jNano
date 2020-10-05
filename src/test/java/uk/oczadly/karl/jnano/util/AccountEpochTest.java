/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util;

import org.junit.Test;
import uk.oczadly.karl.jnano.TestConstants;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.*;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AccountEpochTest {
    
    private final NanoAccount RANDOM_ACC = TestConstants.randAccount();
    
    
    @Test
    public void testComparison() {
        assertTrue(AccountEpoch.V1.compareTo(AccountEpoch.V2) <= -1);
        assertTrue(AccountEpoch.V2.compareTo(AccountEpoch.V1) >= 1);
    }

    @Test
    public void testFromVersion() {
        assertEquals(AccountEpoch.V1, AccountEpoch.fromVersion(1));
        assertEquals(AccountEpoch.V2, AccountEpoch.fromVersion(2));
    }

    @Test
    public void testFromIdentifier() {
        assertEquals(AccountEpoch.V1,
                AccountEpoch.fromIdentifier("65706F636820763120626C6F636B000000000000000000000000000000000000"));
        assertEquals(AccountEpoch.V2,
                AccountEpoch.fromIdentifier("65706F636820763220626C6F636B000000000000000000000000000000000000"));
    }
    
    @Test
    public void testFromBlock() {
        // Not epoch
        assertNull(AccountEpoch.fromEpochBlock(new OpenBlock(null, new WorkSolution(TestConstants.randHex(16)),
                TestConstants.randHexData(64), RANDOM_ACC, RANDOM_ACC)));
        
        // Epoch V2
        assertEquals(AccountEpoch.V2,AccountEpoch.fromEpochBlock(
                new StateBlock(StateBlockSubType.EPOCH, null, new WorkSolution(TestConstants.randHex(16)),
                        RANDOM_ACC, TestConstants.randHexData(64), RANDOM_ACC,
                        new NanoAmount(BigInteger.TEN), AccountEpoch.V2.getIdentifier())));
    }

    @Test
    public void testCalcAccountWithEpoch() {
        List<Block> blocks = new ArrayList<>();
        blocks.add(new OpenBlock(null, new WorkSolution(TestConstants.randHex(16)), TestConstants.randHexData(64),
                RANDOM_ACC, RANDOM_ACC));
        blocks.add(new ChangeBlock(null, new WorkSolution(TestConstants.randHex(16)), TestConstants.randHexData(64),
                RANDOM_ACC));
        blocks.add(new StateBlock(StateBlockSubType.EPOCH, null, new WorkSolution(TestConstants.randHex(16)),
                RANDOM_ACC, TestConstants.randHexData(64), RANDOM_ACC, new NanoAmount(BigInteger.TEN),
                AccountEpoch.V1.getIdentifier())); // EPOCH V1
        blocks.add(new StateBlock(StateBlockSubType.SEND, null, new WorkSolution(TestConstants.randHex(16)),
                RANDOM_ACC, TestConstants.randHexData(64), RANDOM_ACC, new NanoAmount(BigInteger.TEN),
                AccountEpoch.V2.getIdentifier())); // EPOCH V2, but not epoch block
        
        assertEquals(AccountEpoch.V1, AccountEpoch.calculateAccountVersion(blocks));
    }

    @Test
    public void testCalcAccountWithoutEpoch() {
        List<Block> blocks = new ArrayList<>();
        blocks.add(new OpenBlock(null, new WorkSolution(TestConstants.randHex(16)), TestConstants.randHexData(64),
                RANDOM_ACC, RANDOM_ACC));
        blocks.add(new ChangeBlock(null, new WorkSolution(TestConstants.randHex(16)), TestConstants.randHexData(64),
                RANDOM_ACC));
        blocks.add(new StateBlock(StateBlockSubType.SEND, null, new WorkSolution(TestConstants.randHex(16)),
                RANDOM_ACC, TestConstants.randHexData(64), RANDOM_ACC, new NanoAmount(BigInteger.TEN),
                AccountEpoch.V1.getIdentifier())); // EPOCH V1, but not epoch block
    
        assertNull(AccountEpoch.calculateAccountVersion(blocks));
    }
    
}