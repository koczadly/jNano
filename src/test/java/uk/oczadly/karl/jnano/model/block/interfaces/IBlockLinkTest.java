/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block.interfaces;

import org.junit.Test;
import uk.oczadly.karl.jnano.TestConstants;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.block.StateBlock;

import static org.junit.Assert.assertEquals;

/**
 * @author Karl Oczadly
 */
public class IBlockLinkTest {
    
    @Test
    public void testLinkFormat() {
        StateBlock sb = TestConstants.randStateBlock().build();
        assertEquals(sb.getLinkAsAccount().toAddress(), IBlockLink.LinkFormat.ACCOUNT.getBlockLink(sb));
        assertEquals(sb.getLinkData(), IBlockLink.LinkFormat.DATA.getBlockLink(sb));
        assertEquals(JNH.ZEROES_64, IBlockLink.LinkFormat.EMPTY.getBlockLink(sb));
    }
    
}