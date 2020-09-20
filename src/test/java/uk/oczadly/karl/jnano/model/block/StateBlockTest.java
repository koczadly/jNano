/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import org.junit.Before;
import org.junit.Test;
import uk.oczadly.karl.jnano.TestConstants;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockLink;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class StateBlockTest {
    
    StateBlockBuilder builder;
    
    @Before
    public void setup() {
        builder = new StateBlockBuilder()
                .setSubtype(StateBlockSubType.SEND)
                .setAccountAddress("nano_3dmtrrws3pocycmbqwawk6xs7446qxa36fcncush4s1pejk16ksbmakis78m")
                .setRepresentativeAddress("nano_34prihdxwz3u4ps8qjnn14p7ujyewkoxkwyxm3u665it8rg5rdqw84qrypzk")
                .setPreviousBlockHash("90204CCDFB3E7B15F5AA79B4DED8E7268826853231B67B2C16DB37559D578488")
                .setBalance(new BigInteger("1234567"));
    }
    
    @Test
    public void testCalcHash() {
        StateBlock block = builder
                .setLinkData("80204CCDFB3E7B15F5AA79B4DED8E7268826853231B67B2C16DB37559D578488")
                .build();
        
        // Hash
        assertEquals("AC762C4D4E8501026152DA37FBFB00D5A5FB55CDD85835CA4A2354717512203C",
                block.getHash());
        
        // Account link
        assertEquals(NanoAccount.parseAddress("nano_3131bm8zphmu4qttnyfnuueggbna6t4m6efphep3fpsqcpgoh36ajd4c5w55"),
                block.getLinkAsAccount());
    }
    
    @Test
    public void testCalcLinkAccount() {
        StateBlock block = builder
                .setLinkData("80204CCDFB3E7B15F5AA79B4DED8E7268826853231B67B2C16DB37559D578488")
                .build();
        
        // Account link
        assertEquals(NanoAccount.parseAddress("nano_3131bm8zphmu4qttnyfnuueggbna6t4m6efphep3fpsqcpgoh36ajd4c5w55"),
                block.getLinkAsAccount());
    }
    
    @Test
    public void testCalcLinkData() {
        StateBlock block = builder
                .setLinkAccount(NanoAccount.parseAddress("nano_3131bm8zphmu4qttnyfnuueggbna6t4m6efphep3fpsqcpgoh36ajd4c5w55"))
                .build();
        
        // Account link
        assertEquals("80204CCDFB3E7B15F5AA79B4DED8E7268826853231B67B2C16DB37559D578488", block.getLinkData());
    }
    
    @Test
    public void equalityCheck() {
        StateBlock block1 = builder
                .setLinkAccount(NanoAccount.parseAddress("nano_3131bm8zphmu4qttnyfnuueggbna6t4m6efphep3fpsqcpgoh36ajd4c5w55"))
                .build();
        
        StateBlock block2 = builder
                .setLinkAccount(NanoAccount.parseAddress("nano_3131bm8zphmu4qttnyfnuueggbna6t4m6efphep3fpsqcpgoh36ajd4c5w55"))
                .build();
        
        StateBlock block3 = builder
                .setLinkData("62204CCDFB3E7B15F5AA79B4DED8E7268826853231B67B2C16DB37559D578488")
                .build();
    
        assertEquals(block1, block2);
        assertNotEquals(block1, block3);
        assertEquals(block1.hashCode(), block2.hashCode());
    }
    
    @Test
    public void testHashing() {
        StateBlock b = builder
                .setLinkData("62204CCDFB3E7B15F5AA79B4DED8E7268826853231B67B2C16DB37559D578488")
                .build();
        
        assertEquals("3D49EFB46E7716220B5E83A7830F543CC4A3EE50E53183D1E3BE81B2A50B5EFE", b.getHash());
    }
    
    @Test
    public void testIntent() {
        BlockIntent intent;
        
        // Test receive
        intent = TestConstants.randStateBlock()
                .setSubtype(StateBlockSubType.RECEIVE)
                .build().getIntent();
        assertEquals(BlockIntent.UncertainBool.UNKNOWN, intent.isChangeRep());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isEpochUpgrade());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isFirstBlock());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isReceiveFunds());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isSendFunds());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isGenesis());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isTransactional());
        assertEquals(BlockIntent.UncertainBool.UNKNOWN, intent.isSpecial());
        
        // Test send
        intent = TestConstants.randStateBlock()
                .setSubtype(StateBlockSubType.SEND)
                .build().getIntent();
        assertEquals(BlockIntent.UncertainBool.UNKNOWN, intent.isChangeRep());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isEpochUpgrade());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isFirstBlock());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isReceiveFunds());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isSendFunds());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isGenesis());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isTransactional());
        assertEquals(BlockIntent.UncertainBool.UNKNOWN, intent.isSpecial());
    
        // Test open (with OPEN subtype)
        intent = TestConstants.randStateBlock()
                .setSubtype(StateBlockSubType.OPEN)
                .build().getIntent();
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isChangeRep());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isEpochUpgrade());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isFirstBlock());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isReceiveFunds());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isSendFunds());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isGenesis());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isTransactional());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isSpecial());
        
        // Test change
        intent = TestConstants.randStateBlock()
                .setSubtype(StateBlockSubType.CHANGE)
                .build().getIntent();
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isChangeRep());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isEpochUpgrade());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isFirstBlock());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isReceiveFunds());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isSendFunds());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isGenesis());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isTransactional());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isSpecial());
    
        // Test epoch
        intent = TestConstants.randStateBlock()
                .setSubtype(StateBlockSubType.EPOCH)
                .build().getIntent();
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isChangeRep());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isEpochUpgrade());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isFirstBlock());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isReceiveFunds());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isSendFunds());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isGenesis());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isTransactional());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isSpecial());
    
        // Test epoch (with OPEN subtype)
        intent = TestConstants.randStateBlock()
                .setSubtype(StateBlockSubType.OPEN)
                .setPreviousBlockHash(JNH.ZEROES_64)
                .setBalance(BigInteger.ZERO)
                .build().getIntent();
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isChangeRep());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isEpochUpgrade());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isFirstBlock());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isReceiveFunds());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isSendFunds());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isGenesis());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isTransactional());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isSpecial());
    }
    
    @Test
    public void testLinkType() {
        assertEquals(IBlockLink.LinkType.DESTINATION,
                TestConstants.randStateBlock().setSubtype(StateBlockSubType.SEND).build().getLinkType());
        assertEquals(IBlockLink.LinkType.SOURCE_HASH,
                TestConstants.randStateBlock().setSubtype(StateBlockSubType.RECEIVE).build().getLinkType());
        assertEquals(IBlockLink.LinkType.SOURCE_HASH,
                TestConstants.randStateBlock().setSubtype(StateBlockSubType.OPEN).build().getLinkType());
        assertEquals(IBlockLink.LinkType.EPOCH_IDENTIFIER,
                TestConstants.randStateBlock().setSubtype(StateBlockSubType.EPOCH).build().getLinkType());
        assertEquals(IBlockLink.LinkType.NOT_USED,
                TestConstants.randStateBlock().setSubtype(StateBlockSubType.CHANGE).build().getLinkType());
    }
    
}