/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util;

import org.junit.Test;
import uk.oczadly.karl.jnano.internal.NanoConst;
import uk.oczadly.karl.jnano.model.HexData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WalletUtilTest {
    
    @Test
    public void generateSeed() {
        HexData seed = WalletUtil.generateRandomSeed();
        assertNotNull(seed);
        assertEquals(NanoConst.LEN_KEY_B, seed.length());
    }
    
    @Test
    public void derivePrivateKey() {
        HexData seed = new HexData("4F4648622C812DD194E0FAAB0A5BA2DFE6391E807FCDD814FE3FBB28A9919136");
        assertEquals(new HexData("A09DAA9C69FD79A940B51ADC04DF5C9F5693F8445B8B02EFA6A9C9DC650C586B"),
                WalletUtil.deriveKeyFromSeed(seed, 24));
        assertEquals(new HexData("FA91C5FFA80C6A99A4D5F8FB0928D69269546F43BB51FD335787848D0F740C52"),
                WalletUtil.deriveKeyFromSeed(seed, -1));
    }

}