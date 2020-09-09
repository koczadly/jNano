/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model;

import com.google.gson.JsonParser;
import org.junit.Test;
import uk.oczadly.karl.jnano.internal.JNH;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class NanoAccountTest {
    
    static final String ACC_1_PREFIX = "ban";
    static final String ACC_1_ADDR = "ban_38qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qwax3wk9c";
    static final String ACC_1_ADDRSEG = "38qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6q";
    static final String ACC_1_CHECKSUM = "wax3wk9c";
    static final String ACC_1_PUBKEY = "9AF1B28DA06C9CA2466159428733B971068BF154DBA2AB10372510D52E86CC97";
    
    static final String ACC_2_PREFIX = NanoAccount.DEFAULT_PREFIX;
    static final String ACC_2_ADDR = NanoAccount.DEFAULT_PREFIX +
            "_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz";
    static final String ACC_2_PUBKEY = "8AF1B28DA06C9CA2466159428733B971068BF154DBA2AB10372510D52E86CC97";
    
    static final String INVALID_ADDR = "nano_38qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qwax3wk9a";
    static final String INVALID_CHECKSUM = "wax3wk9a";
    
    
    
    @Test
    public void testParse() {
        assertEquals(ACC_2_PUBKEY, NanoAccount.parse(ACC_2_ADDR).toPublicKey()); // Account
        assertNull(NanoAccount.parse(ACC_1_ADDRSEG + ACC_1_CHECKSUM).getPrefix()); // Account (no prefix)
        assertEquals(ACC_1_PUBKEY, NanoAccount.parse(ACC_1_ADDRSEG).toPublicKey()); // Account segment
        assertEquals(ACC_2_ADDR, NanoAccount.parse(ACC_2_PUBKEY).toAddress()); // Public key
        
        // Invalid formats
        assertThrows(NanoAccount.AddressFormatException.class,
                () -> NanoAccount.parse("bumbaclaaat"));
        assertThrows(NanoAccount.AddressFormatException.class,
                () -> NanoAccount.parse("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG"));
    }
    
    @Test
    public void testIndex() {
        assertEquals(NanoAccount.parsePublicKey(JNH.ZEROES_64), new NanoAccount(BigInteger.ZERO));
        assertEquals(new BigInteger(ACC_1_PUBKEY, 16), NanoAccount.parsePublicKey(ACC_1_PUBKEY).getAccountIndex());
        assertEquals(ACC_1_PUBKEY, new NanoAccount(new BigInteger(ACC_1_PUBKEY, 16)).toPublicKey());
    }
    
    @Test
    public void testParsePublicKey() {
        assertEquals(ACC_1_ADDR, NanoAccount.parsePublicKey(ACC_1_PUBKEY, ACC_1_PREFIX).toAddress());
    }
    
    @Test
    public void testParseAddress() {
        // Test valid parses
        NanoAccount addr = NanoAccount.parseAddress(ACC_1_ADDR);
        assertEquals(ACC_1_ADDR, addr.toAddress());
        assertEquals(ACC_1_PREFIX, addr.getPrefix());
        
        assertEquals(ACC_1_PUBKEY,
                NanoAccount.parseAddressSegment(ACC_1_ADDRSEG, ACC_1_PREFIX).toPublicKey());
    
        assertEquals(ACC_1_PUBKEY,
                NanoAccount.parseAddressSegment(ACC_1_ADDRSEG, ACC_1_PREFIX, ACC_1_CHECKSUM)
                        .toPublicKey());
        
        // Test invalid checksums
        assertThrows(NanoAccount.AddressFormatException.class,
                () -> NanoAccount.parseAddress(INVALID_ADDR));
        assertThrows(NanoAccount.AddressFormatException.class,
                () -> NanoAccount.parseAddressSegment(ACC_1_ADDRSEG, ACC_1_PREFIX, INVALID_CHECKSUM));
    }
    
    @Test
    public void testChangePrefix() {
        NanoAccount addr1 = NanoAccount.parseAddress(ACC_1_ADDR);
        addr1.toAddress(); // Preload value
        NanoAccount addr2 = addr1.withPrefix("slug");
        NanoAccount addr3 = addr1.withPrefix("osama");
    
        assertEquals("slug", addr2.getPrefix());
        assertEquals("slug_" + ACC_1_ADDRSEG + ACC_1_CHECKSUM, addr2.toAddress());
        assertEquals("osama", addr3.getPrefix());
        assertEquals("osama_" + ACC_1_ADDRSEG + ACC_1_CHECKSUM, addr3.toAddress());
        assertEquals(ACC_1_ADDRSEG + ACC_1_CHECKSUM, addr1.withPrefix(null).toAddress());
        assertSame(addr2, addr2.withPrefix("slug"));
    }
    
    @Test
    public void testValidityChecks() {
        assertTrue(NanoAccount.isValid(ACC_1_ADDR)); // No prefix specified
        assertTrue(NanoAccount.isValid(ACC_1_ADDR, (String[])null)); // No prefix specified
        assertTrue(NanoAccount.isValid(ACC_1_ADDR, "slug", ACC_1_PREFIX)); // Right prefix
        assertFalse(NanoAccount.isValid(ACC_1_ADDR, "slug", "nano")); // Wrong prefix
        assertTrue(NanoAccount.isSegmentValid(ACC_1_ADDRSEG, ACC_1_CHECKSUM));
        assertFalse(NanoAccount.isSegmentValid(ACC_1_ADDR, INVALID_CHECKSUM));
        
        // Nano prefix
        assertTrue(NanoAccount.isValidNano(NanoAccount.parseAddress(ACC_1_ADDR).withPrefix("xrb").toAddress()));
        assertTrue(NanoAccount.isValidNano(ACC_2_ADDR));
        assertFalse(NanoAccount.isValidNano(ACC_1_ADDR));
    }
    
    @Test
    public void testGetters() {
        NanoAccount addr = NanoAccount.parsePublicKey(ACC_1_PUBKEY, ACC_1_PREFIX);
        assertEquals(32, addr.getPublicKeyBytes().length);
        assertEquals(5, addr.getChecksumBytes().length);
        assertEquals(ACC_1_PREFIX, addr.getPrefix());
        assertEquals(ACC_1_ADDRSEG, addr.getAddressSegment());
        assertEquals(ACC_1_CHECKSUM, addr.getAddressChecksumSegment());
        assertEquals(ACC_1_PUBKEY, addr.toPublicKey());
        assertEquals(ACC_1_ADDR, addr.toAddress());
    }
    
    @Test
    public void testEquality() {
        NanoAccount addr1 = NanoAccount.parsePublicKey(ACC_1_PUBKEY);
        NanoAccount addr2 = NanoAccount.parsePublicKey(ACC_1_PUBKEY);
        NanoAccount addr3 = NanoAccount.parsePublicKey(ACC_1_PUBKEY, "slug");
        NanoAccount addr4 = NanoAccount.parsePublicKey(ACC_2_PUBKEY);
        assertEquals(addr1, addr2);
        assertEquals(addr1.hashCode(), addr2.hashCode());
        assertNotEquals(addr1, addr3); // Prefix doesnt match
        assertNotEquals(addr1, addr4); // Public key doesnt match
    }
    
    @Test
    public void testEqualityIgnorePrefix() {
        NanoAccount addr1 = NanoAccount.parsePublicKey(ACC_1_PUBKEY, "nano");
        NanoAccount addr2 = NanoAccount.parsePublicKey(ACC_1_PUBKEY, "ban");
        NanoAccount addr3 = NanoAccount.parsePublicKey(ACC_2_PUBKEY, "nano");
        
        assertTrue(addr1.equalsIgnorePrefix(addr2));
        assertTrue(addr2.equalsIgnorePrefix(addr1));
        assertFalse(addr1.equalsIgnorePrefix(addr3));
        assertFalse(addr3.equalsIgnorePrefix(addr1));
    }
    
    @Test
    public void testJsonDeserializer() {
        NanoAccount addr1 = JNH.GSON.fromJson("\"" + ACC_2_ADDR + "\"", NanoAccount.class);
        NanoAccount addr2 = JNH.GSON.fromJson("\"" + ACC_2_PUBKEY + "\"", NanoAccount.class);
        assertEquals(ACC_2_ADDR, addr1.toAddress());
        assertEquals(ACC_2_ADDR, addr2.toAddress());
    }
    
    @Test
    public void testJsonSerializer() {
        NanoAccount addr1 = NanoAccount.parseAddress(ACC_1_ADDR);
        assertEquals(JsonParser.parseString("\"" + ACC_1_ADDR + "\""),
                JNH.GSON.toJsonTree(addr1));
    }
    
}