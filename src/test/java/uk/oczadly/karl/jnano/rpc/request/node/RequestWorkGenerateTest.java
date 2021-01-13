/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.request.node;

import org.junit.Test;
import uk.oczadly.karl.jnano.TestConstants;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.block.StateBlock;

import static org.junit.Assert.assertEquals;

public class RequestWorkGenerateTest {
    
    @Test
    public void testBuilder() {
        // With provided previous hash
        StateBlock sb1 = TestConstants.randStateBlock().build();
        RequestWorkGenerate req1 = new RequestWorkGenerate.Builder(sb1).build();
        assertEquals(sb1.getPrevHash().toHexString(), req1.getRootHash());
    
        // Test with no previous hash
        StateBlock sb2 = TestConstants.randStateBlock()
                .setPreviousBlockHash(JNH.ZEROES_64).build();
        RequestWorkGenerate req2 = new RequestWorkGenerate.Builder(sb2).build();
        assertEquals(sb2.getAccount().toPublicKey(), req2.getRootHash());
    }
    
}