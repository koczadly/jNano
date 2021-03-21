/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import org.junit.Test;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import static org.junit.Assert.*;

public class StateBlockBuilderTest {
    
    private static final HexData PRIVATE_KEY = new HexData(
            "C84927664D2119B0FCCBF677F5B3E7EB3BF061A30C4354C8D8A868EB284553E3");
    private static final NanoAccount ACCOUNT = NanoAccount.parse(
            "nano_15ewqenb5det86mfhisncbf88uo4rcki6onzy1eetnzuzwandhyjjtrhdp69");
    private static final HexData SIGNATURE = new HexData("1C9E26A84959AEC939136AF701BF2E57AE5016A632D5A86C3A1735C111" +
            "A839F4C37A63D313FEBBAEEB724C90E52F80EEFC1D3544C646FE49E973AD041427DA02");
    private static final HexData PREVIOUS = new HexData(
            "1AF1B28DA06C9CA2466159428733B971068BF154DBA2AB10372510D52E86CC97");
    private static final WorkSolution WORK = new WorkSolution("009d175747abbc9e");
    private static final NanoAmount BALANCE = NanoAmount.valueOfRaw(1337);
    private static final HexData LINK_DATA = new HexData(
            "8AF1B28DA06C9CA2466159428733B971068BF154DBA2AB10372510D52E86CC97");
    private static final NanoAccount LINK_ACCOUNT = NanoAccount.parse(
            "nano_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz");
    
    
    public static StateBlockBuilder newBuilder() {
        return new StateBlockBuilder()
                .subtype(StateBlockSubType.SEND)
                .account(ACCOUNT)
                .previous(PREVIOUS)
                .link(LINK_DATA)
                .signature(SIGNATURE)
                .work(WORK)
                .balance(BALANCE);
    }
    

    @Test
    public void testBuildMultiple() {
        StateBlockBuilder builder = newBuilder();
        StateBlock b1 = builder.build();
        StateBlock b2 = builder.build();
        
        assertNotNull(b1);
        assertNotNull(b2);
        assertNotSame(b1, b2);
    }
    
    @Test
    public void testAllValues() {
        StateBlock block = newBuilder().build();
        
        assertEquals(BlockType.STATE, block.getType());
        assertEquals(BALANCE, block.getBalance());
        assertEquals(StateBlockSubType.SEND, block.getSubType());
        assertEquals(ACCOUNT, block.getAccount());
        assertEquals(PREVIOUS, block.getPreviousBlockHash());
        assertEquals(ACCOUNT, block.getRepresentative());
        assertEquals(LINK_DATA, block.getLink().asHex());
        assertEquals(LINK_ACCOUNT, block.getLink().asAccount());
        assertEquals(SIGNATURE, block.getSignature());
        assertEquals(WORK, block.getWorkSolution());
        
        // TODO: test JSON objects
        assertNotNull(block.toJsonObject());
        assertNotNull(block.toJsonString());
    }
    
    @Test
    public void testBuild() {
        StateBlock send = newBuilder().subtype(StateBlockSubType.SEND).build();
        assertEquals(StateBlockSubType.SEND, send.getSubType());
        
        StateBlock open = newBuilder().removeSignature().subtype(StateBlockSubType.OPEN).build();
        assertEquals(StateBlockSubType.OPEN, open.getSubType());
        assertTrue(open.getPreviousBlockHash().isZero());
    
        StateBlock change = newBuilder().removeSignature().subtype(StateBlockSubType.CHANGE).build();
        assertEquals(StateBlockSubType.CHANGE, change.getSubType());
        assertTrue(change.getLink().asHex().isZero());
    }
    
    
    // TODO
//    @Test
//    public void testBuildWorkGen() {
//        StateBlock b = newBuilder().setLink(DATA).generateWork(workGen);
//    }
    
    @Test
    public void testBuildSign() {
        StateBlock b = newBuilder().removeSignature().buildAndSign(PRIVATE_KEY);
        assertEquals(SIGNATURE, b.getSignature());
    }
    
    @Test
    public void testAddressPrefixes() {
        // Using custom prefix
        StateBlock b1 = newBuilder().usingAddressPrefix("ban").build();
        assertEquals("ban", b1.getAccount().getPrefix());
        assertEquals("ban", b1.getRepresentative().getPrefix());
        assertEquals("ban", b1.getLink().asAccount().getPrefix());
    
        // Using prefix in 'account'
        StateBlock b2 = newBuilder().account(ACCOUNT.withPrefix("ban")).build();
        assertEquals("ban", b2.getAccount().getPrefix());
        assertEquals("ban", b2.getRepresentative().getPrefix());
        assertEquals("ban", b2.getLink().asAccount().getPrefix());
    }
    
}