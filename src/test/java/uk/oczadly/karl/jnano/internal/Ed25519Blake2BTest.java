/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.internal;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Karl Oczadly
 */
public class Ed25519Blake2BTest {
    
    static final byte[] PRIV = JNC.ENC_16.decode("2091F47280DEAE6A7B5CE0CAFEA85C330BAD2897DAD896B705814DD963BADE99");
    static final byte[] PUB = JNC.ENC_16.decode("854CBBAE20ABAD2E8DFF056E00B681FBD00D46FF72085716EB913D9E66CFDBE9");
    static final byte[] PUB2 = JNC.ENC_16.decode("095B645B6C0CCCB52DD65218DE613CE13CEA58A850A80C3F704291B698A50417");
    static final byte[][] DATA =
            { JNC.ENC_16.decode("956195CA1223250F2AB7B726B0013CE19BFAEB5E77CD0F2F8FE40D31810804AC") };
    static final byte[] SIG = JNC.ENC_16.decode("4D15E4B60730D177C761B975C94F7633CACEE32A48D0E0CD593C14EB52FC0D2A5F" +
            "F9F59B4A249E24336B0FCDDAEF442436E0DC7F678CE2D07D36FA4B06D1F501");
    
    @Test
    public void testSign() {
        assertArrayEquals(SIG, Ed25519Blake2b.sign(PRIV, DATA));
    }
    
    @Test
    public void testVerify() {
        assertTrue(Ed25519Blake2b.verify(PUB, DATA, SIG));           // Valid
        assertFalse(Ed25519Blake2b.verify(PUB2, DATA, SIG));         // Wrong public key
        assertFalse(Ed25519Blake2b.verify(PUB, DATA, new byte[64])); // Wrong signature
    }
    
    @Test
    public void testDerive() {
       assertArrayEquals(PUB, Ed25519Blake2b.derivePublicKey(PRIV));
    }
    
}