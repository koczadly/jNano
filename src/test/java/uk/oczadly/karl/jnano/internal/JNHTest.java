package uk.oczadly.karl.jnano.internal;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

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
    
}