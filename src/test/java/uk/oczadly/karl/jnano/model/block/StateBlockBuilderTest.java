/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import org.junit.Test;
import uk.oczadly.karl.jnano.TestConstants;
import uk.oczadly.karl.jnano.internal.JNC;
import uk.oczadly.karl.jnano.model.HexData;
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
                .subtype(StateBlockSubType.SEND)
                .account(ACCOUNT)
                .previous("1AF1B28DA06C9CA2466159428733B971068BF154DBA2AB10372510D52E86CC97")
                .balance("1337");
    }
    

    @Test
    public void testBuildMultiple() {
        StateBlockBuilder builder = newBuilder()
                .link(TestConstants.randHash());
        
        StateBlock b1 = builder.build();
        StateBlock b2 = builder.build();
        
        assertNotNull(b1);
        assertNotNull(b2);
        assertNotSame(b1, b2);
    }
    
    @Test
    public void testAllValues() {
        StateBlock block = newBuilder()
                .link(DATA)
                .signature("34F1B28DA06C9CA2466159428733B971068BF154DBA2AB10372510D52E86CC9734F1B28DA06C9CA246615" +
                        "9428733B971068BF154DBA2AB10372510D52E86CC97")
                .work(new WorkSolution("009d175747abbc9e"))
                .build();
    
        assertEquals(BlockType.STATE, block.getType());
    
        assertEquals(NanoAmount.valueOfRaw("1337"), block.getBalance());
        assertEquals(StateBlockSubType.SEND, block.getSubType());
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
        StateBlock b1 = newBuilder().link(ACCOUNT).link(DATA).build();
        assertEquals(DATA, b1.getLink().asHex().toString());
        assertEquals(ACCOUNT, b1.getLink().asAccount());
    
        // Account
        StateBlock b2 = newBuilder().link(DATA).link(ACCOUNT).build();
        assertEquals(DATA, b2.getLink().asHex().toString());
        assertEquals(ACCOUNT, b2.getLink().asAccount());
        
        // Null CHANGE subtype should be zeroes
        assertEquals(JNC.ZEROES_64_HD, newBuilder()
                .subtype(StateBlockSubType.CHANGE).build()
                .getLink().asHex());
    }
    
    // TODO
//    @Test
//    public void testBuildWorkGen() {
//        StateBlock b = newBuilder().setLink(DATA).generateWork(workGen);
//    }
    
    @Test
    public void testBuildSign() {
        StateBlock b = newBuilder().account((NanoAccount)null).link(DATA)
                .usingAddressPrefix("ban")
                .buildAndSign(new HexData("1AF1B28DA06C9CA2466159428733B971068BF154DBA2AB10372510D52E86CC97"));
        assertEquals("A73A4178198943EDE5A14696A4F4B6E6F5AD051F9E49F1D10F8896A9148FA19557AD4A5A5F6250FDC69072CC43BCD" +
                "2B29787171F1BD30060AC4D3BD6C862D30E", b.getSignature().toHexString());
        assertEquals("ban", b.getAccount().getPrefix());
    }
    
    @Test
    public void testAddressPrefixes() {
        // Using custom prefix
        StateBlock b1 = newBuilder().link(ACCOUNT).usingAddressPrefix("ban").build();
        assertEquals("ban", b1.getAccount().getPrefix());
        assertEquals("ban", b1.getRepresentative().getPrefix());
        assertEquals("ban", b1.getLink().asAccount().getPrefix());
    
        // Using prefix in 'account'
        StateBlock b2 = newBuilder().account(ACCOUNT.withPrefix("ban")).link(DATA).build();
        assertEquals("ban", b2.getAccount().getPrefix());
        assertEquals("ban", b2.getRepresentative().getPrefix());
        assertEquals("ban", b2.getLink().asAccount().getPrefix());
    }
    
}