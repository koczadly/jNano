package uk.oczadly.karl.jnano.internal;

import org.junit.Test;

import static org.junit.Assert.*;

public class JNHTest {
    
    @Test
    public void testPadArray() {
        byte[] bytes = new byte[] {1, 2, 3};
    
        assertArrayEquals(new byte[] {0, 0, 1, 2, 3}, JNH.leftPadByteArray(bytes, 5, false));
        assertArrayEquals(bytes, JNH.leftPadByteArray(bytes, 3, false));
        assertArrayEquals(bytes, JNH.leftPadByteArray(bytes, 3, true));
        assertArrayEquals(bytes, JNH.leftPadByteArray(bytes, 2, false));
        assertArrayEquals(new byte[] {2, 3}, JNH.leftPadByteArray(bytes, 2, true));
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
    public void testNullable() {
        Object obj1 = "slug", obj2 = 42;
        assertEquals(7, (int)JNH.instanceOf(obj1, String.class, 5, s -> (s.length() == 4 ? 6 : 7)));
        assertEquals(6, (int)JNH.instanceOf(obj1, String.class, 5, s -> (s.length() == 0 ? 6 : 7)));
        assertEquals(5, (int)JNH.instanceOf(obj2, String.class, 5, s -> (s.length() == 4 ? 6 : 7)));
    }
    
}