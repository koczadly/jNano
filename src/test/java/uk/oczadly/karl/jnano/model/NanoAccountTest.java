package uk.oczadly.karl.jnano.model;

import com.google.gson.JsonParser;
import org.junit.Test;
import uk.oczadly.karl.jnano.internal.JNanoHelper;

import static org.junit.Assert.*;

public class NanoAccountTest {
    
    static final String ACC_1_PREFIX = "ban";
    static final String ACC_1_ADDR = "ban_38qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qwax3wk9c";
    static final String ACC_1_ADDRSEG = "38qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6q";
    static final String ACC_1_CHECKSUM = "wax3wk9c";
    static final String ACC_1_PUBKEY = "9AF1B28DA06C9CA2466159428733B971068BF154DBA2AB10372510D52E86CC97";
    
    static final String ACC_2_PREFIX = "nano";
    static final String ACC_2_ADDR = "nano_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz";
    static final String ACC_2_PUBKEY = "8AF1B28DA06C9CA2466159428733B971068BF154DBA2AB10372510D52E86CC97";
    
    static final String INVALID_ADDR = "nano_38qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qwax3wk9a";
    static final String INVALID_CHECKSUM = "wax3wk9a";
    
    
    @Test
    public void testParsing() {
        // Test valid parses
        assertEquals(ACC_1_ADDR,
                NanoAccount.parsePublicKey(ACC_1_PUBKEY, ACC_1_PREFIX).toAddress());
        
        NanoAccount addr = NanoAccount.parse(ACC_1_ADDR);
        assertEquals(ACC_1_ADDR, addr.toAddress());
        assertEquals(ACC_1_PREFIX, addr.getPrefix());
    
        assertEquals(ACC_1_PUBKEY,
                NanoAccount.parseSegment(ACC_1_ADDRSEG, ACC_1_PREFIX).toPublicKey());
    
        assertEquals(ACC_1_PUBKEY,
                NanoAccount.parseSegment(ACC_1_ADDRSEG, ACC_1_PREFIX, ACC_1_CHECKSUM)
                        .toPublicKey());
        
        // Test invalid checksums
        assertThrows(NanoAccount.AddressFormatException.class,
                () -> NanoAccount.parse(INVALID_ADDR));
        assertThrows(NanoAccount.AddressFormatException.class,
                () -> NanoAccount.parseSegment(ACC_1_ADDRSEG, ACC_1_PREFIX, INVALID_CHECKSUM));
    }
    
    @Test
    public void testCloneWithPrefix() {
        NanoAccount addr1 = NanoAccount.parse(ACC_1_ADDR);
        addr1.toAddress(); // Preload value
        NanoAccount addr2 = new NanoAccount(addr1, "slug");
        
        assertEquals("slug", addr2.getPrefix());
        assertEquals("slug_" + ACC_1_ADDRSEG + ACC_1_CHECKSUM, addr2.toAddress());
    }
    
    @Test
    public void testValidityChecks() {
        assertTrue(NanoAccount.isValid(ACC_1_ADDR)); // No prefix specified
        assertTrue(NanoAccount.isValid(ACC_1_ADDR, ACC_1_PREFIX)); // Right prefix
        assertFalse(NanoAccount.isValid(ACC_1_ADDR, "nano")); // Wrong prefix
        assertTrue(NanoAccount.isSegmentValid(ACC_1_ADDRSEG, ACC_1_CHECKSUM));
        assertFalse(NanoAccount.isSegmentValid(ACC_1_ADDR, INVALID_CHECKSUM));
        
        // Nano prefix
        assertTrue(NanoAccount.isValidNano(new NanoAccount(NanoAccount.parse(ACC_1_ADDR), "xrb").toAddress()));
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
        NanoAccount addr3 = NanoAccount.parsePublicKey(ACC_2_PUBKEY);
    
        assertEquals(addr1, addr2);
        assertEquals(addr1.hashCode(), addr2.hashCode());
        assertNotEquals(addr1, addr3);
    }
    
    @Test
    public void testJsonDeserializer() {
        NanoAccount addr1 = JNanoHelper.GSON.fromJson("\"" + ACC_2_ADDR + "\"", NanoAccount.class);
        NanoAccount addr2 = JNanoHelper.GSON.fromJson("\"" + ACC_2_PUBKEY + "\"", NanoAccount.class);
        assertEquals(ACC_2_ADDR, addr1.toAddress());
        assertEquals(ACC_2_ADDR, addr2.toAddress());
    }
    
    @Test
    public void testJsonSerializer() {
        NanoAccount addr1 = NanoAccount.parse(ACC_1_ADDR);
        assertEquals(JsonParser.parseString("\"" + ACC_1_ADDR + "\""),
                JNanoHelper.GSON.toJsonTree(addr1));
    }
    
}