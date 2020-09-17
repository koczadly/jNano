/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.internal;

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
        assertFalse(JNH.isValidHex("AS", 2));
        assertFalse(JNH.isValidHex("AB", 4));
    }
    
    @Test
    public void testByteLong() {
        byte[] arr = new byte[] {1, 2, 3, 4, 5, 6, 7, -24};
        assertEquals(72623859790383080L, JNH.bytesToLong(arr));
        assertArrayEquals(arr, JNH.longToBytes(72623859790383080L));
    }
    
    @Test
    public void testIsZero() {
        assertTrue(JNH.isZero(null, true));
        assertFalse(JNH.isZero(null, false));
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
    public void testNullable() {
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
}