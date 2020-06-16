package uk.oczadly.karl.jnano.util;

import org.junit.Test;
import uk.oczadly.karl.jnano.TestConstants;
import uk.oczadly.karl.jnano.model.block.*;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountEpochTest {
    
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
    public void testCalcAccountWithEpoch() {
        List<Block> blocks = new ArrayList<>();
        blocks.add(new OpenBlock(null, new WorkSolution(TestConstants.hex(16)), TestConstants.hex(64),
                TestConstants.account(0), TestConstants.account(0)));
        blocks.add(new ChangeBlock(null, new WorkSolution(TestConstants.hex(16)), TestConstants.hex(64),
                TestConstants.account(0)));
        blocks.add(new StateBlock(StateBlockSubType.EPOCH, null, new WorkSolution(TestConstants.hex(16)),
                TestConstants.account(0), TestConstants.hex(64), TestConstants.account(0), BigInteger.TEN,
                AccountEpoch.V1.getIdentifier())); // EPOCH V1
        blocks.add(new StateBlock(StateBlockSubType.SEND, null, new WorkSolution(TestConstants.hex(16)),
                TestConstants.account(0), TestConstants.hex(64), TestConstants.account(0), BigInteger.TEN,
                AccountEpoch.V2.getIdentifier())); // EPOCH V2, but not epoch block
        
        assertEquals(AccountEpoch.V1, AccountEpoch.calculateAccountVersion(blocks));
    }

    @Test
    public void testCalcAccountWithoutEpoch() {
        List<Block> blocks = new ArrayList<>();
        blocks.add(new OpenBlock(null, new WorkSolution(TestConstants.hex(16)), TestConstants.hex(64),
                TestConstants.account(0), TestConstants.account(0)));
        blocks.add(new ChangeBlock(null, new WorkSolution(TestConstants.hex(16)), TestConstants.hex(64),
                TestConstants.account(0)));
        blocks.add(new StateBlock(StateBlockSubType.SEND, null, new WorkSolution(TestConstants.hex(16)),
                TestConstants.account(0), TestConstants.hex(64), TestConstants.account(0),
                BigInteger.TEN, AccountEpoch.V1.getIdentifier())); // EPOCH V1, but not epoch block
        
        assertEquals(AccountEpoch.LATEST_EPOCH, AccountEpoch.calculateAccountVersion(blocks));
    }
    
}