/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util;

import org.junit.Test;
import uk.oczadly.karl.jnano.TestConstants;
import uk.oczadly.karl.jnano.model.block.StateBlockBuilder;
import uk.oczadly.karl.jnano.model.block.StateBlockSubType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Karl Oczadly
 */
public class NetworkConstantsTest {
    
    @Test
    public void testVersionCompare() {
        NetworkConstants.Version ver1 = new NetworkConstants.Version(10, 2);
        NetworkConstants.Version ver2 = new NetworkConstants.Version(10, 2);
        NetworkConstants.Version ver3 = new NetworkConstants.Version(10, 3);
        NetworkConstants.Version ver4 = new NetworkConstants.Version(9, 2);
        assertTrue(ver1.compareTo(ver2) == 0);
        assertTrue(ver1.compareTo(ver3) < 0);
        assertTrue(ver1.compareTo(ver4) > 0);
    }
    
    @Test
    public void testVersionToString() {
        NetworkConstants.Version ver1 = new NetworkConstants.Version(10, 2);
        NetworkConstants.Version ver2 = new NetworkConstants.Version(10, 0);
        assertEquals("V10.2", ver1.toString());
        assertEquals("V10.0", ver2.toString());
    }
    
    @Test
    public void testWorkV2() {
        long diffSend = 0xfffffff800000000L;
        long diffReceive = 0xfffffe0000000000L;
        long diffLegacy = 0xffffffc000000000L;
        NetworkConstants.WorkDifficulties diff = new NetworkConstants.WorkDifficultiesV2(
                diffSend, diffReceive, diffLegacy);
        
        // Base
        assertEquals(diffSend, diff.getBase().getAsLong());
        // State block types
        assertEquals(diffSend, diff.getForType(StateBlockSubType.SEND).getAsLong());
        assertEquals(diffSend, diff.getForType(StateBlockSubType.CHANGE).getAsLong());
        assertEquals(diffReceive, diff.getForType(StateBlockSubType.RECEIVE).getAsLong());
        assertEquals(diffReceive, diff.getForType(StateBlockSubType.OPEN).getAsLong());
        assertEquals(diffReceive, diff.getForType(StateBlockSubType.EPOCH).getAsLong());
        // From legacy block
        assertEquals(diffLegacy, diff.getForBlock(TestConstants.randOpenBlock()).getAsLong());
        assertEquals(diffLegacy, diff.getForBlock(TestConstants.randChangeBlock()).getAsLong());
        assertEquals(diffLegacy, diff.getForBlock(TestConstants.randSendBlock()).getAsLong());
        assertEquals(diffLegacy, diff.getForBlock(TestConstants.randReceiveBlock()).getAsLong());
        // From state block
        StateBlockBuilder sb = TestConstants.randStateBlock();
        assertEquals(diffSend, diff.getForBlock(sb.setSubtype(StateBlockSubType.SEND).build()).getAsLong());
        assertEquals(diffSend, diff.getForBlock(sb.setSubtype(StateBlockSubType.CHANGE).build()).getAsLong());
        assertEquals(diffReceive, diff.getForBlock(sb.setSubtype(StateBlockSubType.RECEIVE).build()).getAsLong());
        assertEquals(diffReceive, diff.getForBlock(sb.setSubtype(StateBlockSubType.OPEN).build()).getAsLong());
        assertEquals(diffReceive, diff.getForBlock(sb.setSubtype(StateBlockSubType.EPOCH).build()).getAsLong());
    }
    
}