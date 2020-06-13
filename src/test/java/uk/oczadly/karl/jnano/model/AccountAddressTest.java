package uk.oczadly.karl.jnano.model;

import com.google.gson.JsonParser;
import org.junit.Test;
import uk.oczadly.karl.jnano.internal.JNanoHelper;

import static org.junit.Assert.*;

public class AccountAddressTest {
    
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
                AccountAddress.parsePublicKey(ACC_1_PUBKEY, ACC_1_PREFIX).getAsAddress());
        
        AccountAddress addr = AccountAddress.parse(ACC_1_ADDR);
        assertEquals(ACC_1_ADDR, addr.getAsAddress());
        assertEquals(ACC_1_PREFIX, addr.getPrefix());
    
        assertEquals(ACC_1_PUBKEY,
                AccountAddress.parseSegment(ACC_1_ADDRSEG, ACC_1_PREFIX).getAsPublicKey());
    
        assertEquals(ACC_1_PUBKEY,
                AccountAddress.parseSegment(ACC_1_ADDRSEG, ACC_1_PREFIX, ACC_1_CHECKSUM)
                        .getAsPublicKey());
        
        // Test invalid checksums
        assertThrows(AccountAddress.AddressFormatException.class,
                () -> AccountAddress.parse(INVALID_ADDR));
        assertThrows(AccountAddress.AddressFormatException.class,
                () -> AccountAddress.parseSegment(ACC_1_ADDRSEG, ACC_1_PREFIX, INVALID_CHECKSUM));
    }
    
    @Test
    public void testClone() {
        AccountAddress addr1 = AccountAddress.parse(ACC_1_ADDR);
        AccountAddress addr2 = new AccountAddress(addr1);
        
        assertEquals(addr1.getPrefix(), addr2.getPrefix());
        assertArrayEquals(addr1.getPublicKeyBytes(), addr2.getPublicKeyBytes());
        assertArrayEquals(addr1.getChecksumBytes(), addr2.getChecksumBytes());
        assertEquals(addr1.getAsAddress(), addr2.getAsAddress());
        assertEquals(addr1.getAddressSegment(), addr2.getAddressSegment());
        assertEquals(addr1.getAddressChecksumSegment(), addr2.getAddressChecksumSegment());
        assertEquals(addr1.getAsPublicKey(), addr2.getAsPublicKey());
        assertEquals(addr1, addr2);
    }
    
    @Test
    public void testCloneWithPrefix() {
        AccountAddress addr1 = AccountAddress.parse(ACC_1_ADDR);
        addr1.getAsAddress(); // Preload value
        AccountAddress addr2 = new AccountAddress(addr1, "slug");
        
        assertEquals("slug", addr2.getPrefix());
        assertEquals("slug_" + ACC_1_ADDRSEG + ACC_1_CHECKSUM, addr2.getAsAddress());
    }
    
    @Test
    public void testValidityChecks() {
        assertTrue(AccountAddress.isValid(ACC_1_ADDR)); // No prefix specified
        assertTrue(AccountAddress.isValid(ACC_1_ADDR, ACC_1_PREFIX)); // Right prefix
        assertFalse(AccountAddress.isValid(ACC_1_ADDR, "nano")); // Wrong prefix
        assertTrue(AccountAddress.isSegmentValid(ACC_1_ADDRSEG, ACC_1_CHECKSUM));
        assertFalse(AccountAddress.isSegmentValid(ACC_1_ADDR, INVALID_CHECKSUM));
    }
    
    @Test
    public void testGetters() {
        AccountAddress addr = AccountAddress.parsePublicKey(ACC_1_PUBKEY, ACC_1_PREFIX);
        assertEquals(32, addr.getPublicKeyBytes().length);
        assertEquals(5, addr.getChecksumBytes().length);
        assertEquals(ACC_1_PREFIX, addr.getPrefix());
        assertEquals(ACC_1_ADDRSEG, addr.getAddressSegment());
        assertEquals(ACC_1_CHECKSUM, addr.getAddressChecksumSegment());
        assertEquals(ACC_1_PUBKEY, addr.getAsPublicKey());
        assertEquals(ACC_1_ADDR, addr.getAsAddress());
    }
    
    @Test
    public void testEquality() {
        AccountAddress addr1 = AccountAddress.parsePublicKey(ACC_1_PUBKEY);
        AccountAddress addr2 = AccountAddress.parsePublicKey(ACC_1_PUBKEY);
        AccountAddress addr3 = AccountAddress.parsePublicKey(ACC_2_PUBKEY);
    
        assertEquals(addr1, addr2);
        assertEquals(addr1.hashCode(), addr2.hashCode());
        assertNotEquals(addr1, addr3);
    }
    
    @Test
    public void testJsonDeserializer() {
        AccountAddress addr1 = JNanoHelper.GSON.fromJson("\"" + ACC_2_ADDR + "\"", AccountAddress.class);
        AccountAddress addr2 = JNanoHelper.GSON.fromJson("\"" + ACC_2_PUBKEY + "\"", AccountAddress.class);
        assertEquals(ACC_2_ADDR, addr1.getAsAddress());
        assertEquals(ACC_2_ADDR, addr2.getAsAddress());
    }
    
    @Test
    public void testJsonSerializer() {
        AccountAddress addr1 = AccountAddress.parse(ACC_1_ADDR);
        assertEquals(JsonParser.parseString("\"" + ACC_1_ADDR + "\""),
                JNanoHelper.GSON.toJsonTree(addr1));
    }
    
}