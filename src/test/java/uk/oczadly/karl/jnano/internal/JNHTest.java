/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.internal;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.junit.Test;
import uk.oczadly.karl.jnano.util.NanoConstants;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class JNHTest {
    
    @Test
    public void testFilledByteArray() {
        byte[] bytes = JNH.filledByteArray(3, (byte)27);
        assertEquals(3, bytes.length);
        assertEquals(27, bytes[0]);
        assertEquals(27, bytes[1]);
        assertEquals(27, bytes[2]);
    }
    
    @Test
    public void testLeftPadArray() {
        byte[] bytes = new byte[] {1, 2, 3};
    
        assertArrayEquals(new byte[] {0, 0, 1, 2, 3}, JNH.leftPadByteArray(bytes, 5, false));
        assertArrayEquals(bytes, JNH.leftPadByteArray(bytes, 3, false));
        assertArrayEquals(bytes, JNH.leftPadByteArray(bytes, 3, true));
        assertArrayEquals(bytes, JNH.leftPadByteArray(bytes, 2, false));
        assertArrayEquals(new byte[] {2, 3}, JNH.leftPadByteArray(bytes, 2, true));
    }
    
    @Test
    public void testReverseByteArray() {
        assertNull(JNH.reverseArray(null));
        assertArrayEquals(new byte[] {4, 3, 2}, JNH.reverseArray(new byte[] {2, 3, 4}));
    }
    
    @Test
    public void testBalanceValid() {
        assertTrue(JNH.isBalanceValid(null));
        assertTrue(JNH.isBalanceValid(BigInteger.ZERO));
        assertTrue(JNH.isBalanceValid(new BigInteger("270")));
        assertTrue(JNH.isBalanceValid(NanoConstants.MAX_BALANCE_RAW));
        assertFalse(JNH.isBalanceValid(NanoConstants.MAX_BALANCE_RAW.add(BigInteger.ONE)));
        assertFalse(JNH.isBalanceValid(new BigInteger("-1")));
    }
    
    @Test
    public void testIsValidHex() {
        assertTrue(JNH.isValidHex(null, 2));
        assertTrue(JNH.isValidHex("AB", 2));
        assertTrue(JNH.isValidHex("AB", -1));
        assertFalse(JNH.isValidHex("AS", 2));
        assertFalse(JNH.isValidHex("AB", 4));
        assertFalse(JNH.isValidHex("AS", -1));
    }
    
    @Test
    public void testByteLong() {
        byte[] arr = new byte[] {1, 2, 3, 4, 5, 6, 7, -24};
        assertEquals(72623859790383080L, JNH.bytesToLong(arr));
        assertArrayEquals(arr, JNH.longToBytes(72623859790383080L));
    }
    
    @Test
    public void testIsZero() {
        assertTrue(JNH.isZero((String)null, true));
        assertFalse(JNH.isZero((String)null, false));
        assertTrue(JNH.isZero("", true));
        assertTrue(JNH.isZero("0", true));
        assertTrue(JNH.isZero("00000", true));
        assertTrue(JNH.isZero(JNH.ZEROES_64, true));
        assertFalse(JNH.isZero("00000001", true));
        assertFalse(JNH.isZero("04892slugs20", true));
    }
    
    @Test
    public void testRepeatChar() {
        assertEquals("ssss", JNH.repeatChar('s', 4));
    }
    
    @Test
    public void testInstanceOf() {
        Object obj1 = "slug", obj2 = 42;
        assertEquals(101, (int)JNH.instanceOf(obj1, String.class, 100, s -> (s.length() == 4 ? 101 : 102)));
        assertEquals(102, (int)JNH.instanceOf(obj1, String.class, 100, s -> (s.length() == 0 ? 101 : 102)));
        assertEquals(100, (int)JNH.instanceOf(obj2, String.class, 100, s -> (s.length() == 4 ? 101 : 102)));
    }
    
    @Test
    public void leftPadString() {
        assertEquals("slug", JNH.leftPadString("slug", 4, '0'));
        assertEquals("slug", JNH.leftPadString("slug", 2, '0'));
        assertEquals("sluggage", JNH.leftPadString("sluggage", 4, '0'));
        assertEquals("000slug", JNH.leftPadString("slug", 7, '0'));
    }
    
    @Test
    public void testNullable() {
        assertNull(JNH.nullable(null, (String o) -> o + ""));
        assertEquals("valA", JNH.nullable("val", (String o) -> o + "A"));
    }
    
    @Test
    public void testJsonParams() {
        JsonObject obj = new JsonObject();
        obj.addProperty("val1", 101);
    
        // Vals
        assertEquals("101", JNH.getJson(obj, "val1"));
        assertEquals("101A", JNH.getJson(obj, "val1", s -> s + "A"));
        // Nulls
        assertNull(JNH.getJson(obj, "val2"));
        assertNull(JNH.getJson(null, "val2"));
        assertNull(JNH.getJson(obj, "val2", s -> s + "A"));
        assertNull(JNH.getJson(null, "val2", s -> s + "A"));
    }
    
    @Test
    public void testParseJson() {
        assertEquals(123, JNH.parseJson("{\"val\":123}").get("val").getAsInt());
        assertThrows(JsonParseException.class, () -> JNH.parseJson("\"s\""));
        assertThrows(JsonParseException.class, () -> JNH.parseJson("\""));
    }
    
    @Test
    public void testMax() {
        assertEquals(200, (int)JNH.max(4, 7, 2, 9, 200, 1));
        assertEquals(7, (int)JNH.max(4, 7, 2, 7, 3, -4));
    }
    
    @SuppressWarnings("NumericOverflow")
    @Test
    public void testTryRethrow() {
        // Return
        assertEquals(123, (int)JNH.tryRethrow(() -> 123, RuntimeException::new));
        assertEquals(246, (int)JNH.tryRethrow(123, (o) -> o * 2, RuntimeException::new));
        // Exception
        assertThrows(IllegalArgumentException.class, () ->
                JNH.tryRethrow(() -> { throw new Exception(); }, IllegalArgumentException::new));
        assertThrows(IllegalArgumentException.class, () ->
                JNH.tryRethrow("123", (o) -> { throw new Exception(o); }, IllegalArgumentException::new));
    }
    
}