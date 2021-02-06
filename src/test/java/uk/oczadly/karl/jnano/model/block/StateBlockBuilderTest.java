/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import org.junit.Test;
import uk.oczadly.karl.jnano.TestConstants;
import uk.oczadly.karl.jnano.internal.JNC;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import static org.junit.Assert.*;

public class StateBlockBuilderTest {
    
    private static final String DATA = "8AF1B28DA06C9CA2466159428733B971068BF154DBA2AB10372510D52E86CC97";
    private static final NanoAccount ACCOUNT =
            NanoAccount.parseAddress("nano_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz");
    
    public static StateBlockBuilder newBuilder() {
        return new StateBlockBuilder()
                .setSubtype(StateBlockSubType.EPOCH)
                .setAccount(ACCOUNT)
                .setPreviousHash("1AF1B28DA06C9CA2466159428733B971068BF154DBA2AB10372510D52E86CC97")
                .setBalance("1337");
    }
    

    @Test
    public void testBuildMultiple() {
        StateBlockBuilder builder = newBuilder()
                .setLink(TestConstants.randHash());
        
        StateBlock b1 = builder.build();
        StateBlock b2 = builder.build();
        
        assertNotNull(b1);
        assertNotNull(b2);
        assertNotSame(b1, b2);
    }
    
    @Test
    public void testAllValues() {
        StateBlock block = newBuilder()
                .setLink(DATA)
                .setSignature("34F1B28DA06C9CA2466159428733B971068BF154DBA2AB10372510D52E86CC9734F1B28DA06C9CA246615" +
                        "9428733B971068BF154DBA2AB10372510D52E86CC97")
                .setWork(new WorkSolution("009d175747abbc9e"))
                .build();
    
        assertEquals(BlockType.STATE, block.getType());
    
        assertEquals(NanoAmount.valueOfRaw("1337"), block.getBalance());
        assertEquals(StateBlockSubType.EPOCH, block.getSubType());
        assertEquals(ACCOUNT, block.getAccount());
        assertEquals("1AF1B28DA06C9CA2466159428733B971068BF154DBA2AB10372510D52E86CC97",
                block.getPreviousBlockHash().toHexString());
        assertEquals(ACCOUNT, block.getRepresentative());
        assertEquals(DATA, block.getLink().asHex().toString());
        assertEquals("34F1B28DA06C9CA2466159428733B971068BF154DBA2AB10372510D52E86CC9734F1B28DA06C9CA24661594" +
                "28733B971068BF154DBA2AB10372510D52E86CC97", block.getSignature().toHexString());
        assertEquals(new WorkSolution("009d175747abbc9e"), block.getWorkSolution());
        
        //TODO test JSON
        assertNotNull(block.toJsonObject());
        assertNotNull(block.toJsonString());
    }
    
    @Test
    public void testLinkFormats() {
        // Data
        StateBlock b1 = newBuilder().setLink(ACCOUNT).setLink(DATA).build();
        assertEquals(DATA, b1.getLink().asHex().toString());
        assertEquals(ACCOUNT, b1.getLink().asAccount());
    
        // Account
        StateBlock b2 = newBuilder().setLink(DATA).setLink(ACCOUNT).build();
        assertEquals(DATA, b2.getLink().asHex().toString());
        assertEquals(ACCOUNT, b2.getLink().asAccount());
        
        // Null CHANGE subtype should be zeroes
        assertEquals(JNC.ZEROES_64_HD, newBuilder()
                .setSubtype(StateBlockSubType.CHANGE).build()
                .getLink().asHex());
    }
    
}