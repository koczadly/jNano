/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util;

import org.junit.Test;
import uk.oczadly.karl.jnano.internal.JNH;

import static org.junit.Assert.*;

public class WalletUtilTest {
    
    @Test
    public void generateSeed() {
        String seed = WalletUtil.generateRandomSeed();
        assertNotNull(seed);
        assertTrue(JNH.isValidHex(seed, 64));
    }
    
    @Test
    public void derivePrivateKey() {
        String seed = "4F4648622C812DD194E0FAAB0A5BA2DFE6391E807FCDD814FE3FBB28A9919136";
        assertEquals("A09DAA9C69FD79A940B51ADC04DF5C9F5693F8445B8B02EFA6A9C9DC650C586B",
                WalletUtil.deriveKeyFromSeed(seed, 24));
    }

}