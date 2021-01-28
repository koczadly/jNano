/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Karl Oczadly
 */
public class NetworkConstantsTest {
    
    @Test
    public void testVersionCompare() {
        NetworkConstants.Version ver1 = new NetworkConstants.Version(10, 2);
        NetworkConstants.Version ver2 = new NetworkConstants.Version(10, 2);
        NetworkConstants.Version ver3 = new NetworkConstants.Version(10, 3);
        NetworkConstants.Version ver4 = new NetworkConstants.Version(9, 2);
        assertTrue(ver1.compareTo(ver2) == 0);
        assertTrue(ver1.compareTo(ver3) < 0);
        assertTrue(ver1.compareTo(ver4) > 0);
    }
    
    @Test
    public void testVersionToString() {
        NetworkConstants.Version ver1 = new NetworkConstants.Version(10, 2);
        NetworkConstants.Version ver2 = new NetworkConstants.Version(10, 0);
        assertEquals("V10.2", ver1.toString());
        assertEquals("V10.0", ver2.toString());
    }
    
}