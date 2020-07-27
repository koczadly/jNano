package uk.oczadly.karl.jnano.internal;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class JNanoHelperTest {
    
    @Test
    public void testPadArray() {
        byte[] bytes = new byte[] {1, 2, 3};
    
        assertArrayEquals(new byte[] {0, 0, 1, 2, 3}, JNanoHelper.leftPadByteArray(bytes, 5, false));
        assertArrayEquals(bytes, JNanoHelper.leftPadByteArray(bytes, 3, false));
        assertArrayEquals(bytes, JNanoHelper.leftPadByteArray(bytes, 3, true));
        assertArrayEquals(bytes, JNanoHelper.leftPadByteArray(bytes, 2, false));
        assertArrayEquals(new byte[] {2, 3}, JNanoHelper.leftPadByteArray(bytes, 2, true));
    }
    
}