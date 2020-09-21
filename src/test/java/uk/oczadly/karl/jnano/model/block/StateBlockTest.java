/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import org.junit.Test;
import uk.oczadly.karl.jnano.TestConstants;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockLink;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class StateBlockTest {
    
    final String TB_SIGNATURE = "BC226F03E73CDA9706748494DBB1D0B78CE244BE5C66BCF4EBF88FBAF0937A4BEACE5E610B12278ADC6" +
            "322BB6F0297CFB1D1CBF6D51FB331F2B25E0AD4A4A60C";
    final WorkSolution TB_WORK = new WorkSolution("508bc946fe6d22e7");
    final NanoAccount TB_ACCOUNT = NanoAccount.parseAddress(
            "nano_3dmtrrws3pocycmbqwawk6xs7446qxa36fcncush4s1pejk16ksbmakis78m");
    final NanoAccount TB_REP = NanoAccount.parseAddress(
            "nano_34prihdxwz3u4ps8qjnn14p7ujyewkoxkwyxm3u665it8rg5rdqw84qrypzk");
    final String TB_PREVIOUS = "90204CCDFB3E7B15F5AA79B4DED8E7268826853231B67B2C16DB37559D578488";
    final String TB_LINK = "65706F636820763220626C6F636B000000000000000000000000000000000000";
    final NanoAmount TB_BALANCE = new NanoAmount("1234567");
    final StateBlockSubType TB_SUBTYPE = StateBlockSubType.SEND;
    
    final StateBlockBuilder TEST_BUILDER = new StateBlockBuilder()
            .setSignature(TB_SIGNATURE)
            .setWorkSolution(TB_WORK)
            .setSubtype(TB_SUBTYPE)
            .setAccountAddress(TB_ACCOUNT)
            .setRepresentativeAddress(TB_REP)
            .setPreviousBlockHash(TB_PREVIOUS)
            .setBalance(TB_BALANCE)
            .setLinkData("65706F636820763220626C6F636B000000000000000000000000000000000000");
    
    final String TEST_BLOCK_JSON = "{\n" +
            "    \"type\": \"state\",\n" +
            "    \"account\": \"nano_3dmtrrws3pocycmbqwawk6xs7446qxa36fcncush4s1pejk16ksbmakis78m\",\n" +
            "    \"previous\": \"90204CCDFB3E7B15F5AA79B4DED8E7268826853231B67B2C16DB37559D578488\",\n" +
            "    \"representative\": \"nano_34prihdxwz3u4ps8qjnn14p7ujyewkoxkwyxm3u665it8rg5rdqw84qrypzk\",\n" +
            "    \"balance\": \"1234567\",\n" +
            "    \"link\": \"65706F636820763220626C6F636B000000000000000000000000000000000000\",\n" +
            "    \"link_as_account\": \"nano_1sdifxjpia5p8ai86u5hefoi1111111111111111111111111111ngspq7ps\",\n" +
            "    \"signature\": \"BC226F03E73CDA9706748494DBB1D0B78CE244BE5C66BCF4EBF88FBAF0937A4BEACE5E610B12278ADC6" +
            "322BB6F0297CFB1D1CBF6D51FB331F2B25E0AD4A4A60C\",\n" +
            "    \"work\": \"508bc946fe6d22e7\",\n" +
            "    \"subtype\": \"send\"\n" +
            "  }";
    
    final StateBlock TEST_BLOCK = TEST_BUILDER.build();
    
    
    @Test
    public void testParse() {
        StateBlock block = StateBlock.parse(TEST_BLOCK_JSON);
        assertEquals(block, TEST_BLOCK);
        assertEquals(TB_SIGNATURE, block.getSignature());
        assertEquals(TB_WORK, block.getWorkSolution());
        assertEquals(TB_SUBTYPE, block.getSubType());
        assertEquals(TB_ACCOUNT, block.getAccount());
        assertEquals(TB_REP, block.getRepresentative());
        assertEquals(TB_PREVIOUS, block.getPreviousBlockHash());
        assertEquals(TB_BALANCE, block.getBalance());
        assertEquals(TB_LINK, block.getLinkData());
    }
    
    @Test
    public void testCalcHash() {
        StateBlock block = new StateBlockBuilder(TEST_BUILDER)
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
        StateBlock block = new StateBlockBuilder(TEST_BUILDER)
                .setLinkData("80204CCDFB3E7B15F5AA79B4DED8E7268826853231B67B2C16DB37559D578488")
                .build();
        
        // Account link
        assertEquals(NanoAccount.parseAddress("nano_3131bm8zphmu4qttnyfnuueggbna6t4m6efphep3fpsqcpgoh36ajd4c5w55"),
                block.getLinkAsAccount());
    }
    
    @Test
    public void testCalcLinkData() {
        StateBlock block = new StateBlockBuilder(TEST_BUILDER)
                .setLinkAccount(NanoAccount.parseAddress("nano_3131bm8zphmu4qttnyfnuueggbna6t4m6efphep3fpsqcpgoh36ajd4c5w55"))
                .build();
        
        // Account link
        assertEquals("80204CCDFB3E7B15F5AA79B4DED8E7268826853231B67B2C16DB37559D578488", block.getLinkData());
    }
    
    @Test
    public void testEquality() {
        StateBlock block1 = new StateBlockBuilder(TEST_BUILDER)
                .setLinkAccount(NanoAccount.parseAddress("nano_3131bm8zphmu4qttnyfnuueggbna6t4m6efphep3fpsqcpgoh36ajd4c5w55"))
                .build();
        
        StateBlock block2 = new StateBlockBuilder(TEST_BUILDER)
                .setLinkAccount(NanoAccount.parseAddress("nano_3131bm8zphmu4qttnyfnuueggbna6t4m6efphep3fpsqcpgoh36ajd4c5w55"))
                .build();
        
        StateBlock block3 = new StateBlockBuilder(TEST_BUILDER)
                .setLinkData("62204CCDFB3E7B15F5AA79B4DED8E7268826853231B67B2C16DB37559D578488")
                .build();
    
        // Equal
        assertEquals(block1, block2);
        assertTrue(block1.contentEquals(block2));
        assertTrue(block2.contentEquals(block1));
        // Not equal
        assertNotEquals(block1, block3);
        assertFalse(block1.contentEquals(block3));
        assertFalse(block3.contentEquals(block1));
        // Check hashcode
        assertEquals(block1.hashCode(), block2.hashCode());
    }
    
    @Test
    public void testHashing() {
        StateBlock b = new StateBlockBuilder(TEST_BUILDER)
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