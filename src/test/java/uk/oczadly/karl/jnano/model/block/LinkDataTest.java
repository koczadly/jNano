/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import org.junit.Test;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;

import static org.junit.Assert.*;

/**
 * @author Karl Oczadly
 */
public class LinkDataTest {
    
    static final String HEX = "0000000000000000000000000000000000000000000000000000000000000102";
    
    @Test
    public void testAsGetters() {
        LinkData link = create(LinkData.Intent.UNUSED);
    
        assertEquals(HEX, link.asHex().toHexString());
        assertArrayEquals(JNH.leftPadByteArray(new byte[] {1, 2}, 32, false), link.asByteArray());
        assertEquals(NanoAccount.parsePublicKey(HEX), link.asAccount());
    }
    
    @Test
    public void testIntent() {
        assertEquals(LinkData.Intent.DESTINATION_ACCOUNT,
                create(LinkData.Intent.DESTINATION_ACCOUNT).getIntent());
        assertEquals(LinkData.Intent.EPOCH_IDENTIFIER,
                create(LinkData.Intent.EPOCH_IDENTIFIER).getIntent());
    }
    
    @Test
    public void testToString() {
        // Hash
        assertEquals(HEX, create(LinkData.Intent.SOURCE_HASH).toString());
        // Account
        assertEquals("nano_11111111111111111111111111111111111111111111111111a46eiwy9ir",
                create(LinkData.Intent.DESTINATION_ACCOUNT).toString());
        // None
        assertEquals("N/A", create(LinkData.Intent.UNUSED).toString());
    }
    
    @Test
    public void testEquals() {
        // Same
        assertEquals(create(LinkData.Intent.SOURCE_HASH),
                create(LinkData.Intent.SOURCE_HASH));
        assertEquals(create(LinkData.Intent.SOURCE_HASH).hashCode(),
                create(LinkData.Intent.SOURCE_HASH).hashCode());
        // Different intent
        assertNotEquals(create(LinkData.Intent.DESTINATION_ACCOUNT),
                create(LinkData.Intent.SOURCE_HASH));
        // Different data
        assertNotEquals(create(LinkData.Intent.SOURCE_HASH),
                new LinkData(LinkData.Intent.SOURCE_HASH, JNH.ZEROES_64_HD, null));
    }
    
    
    static LinkData create(LinkData.Intent intent) {
        return new LinkData(intent, new HexData(HEX), null);
    }
    
    
}