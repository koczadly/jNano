package uk.oczadly.karl.jnano.model.block;

import org.junit.Before;
import org.junit.Test;
import uk.oczadly.karl.jnano.TestConstants;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.NanoAccount;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class StateBlockTest {
    
    StateBlockBuilder builder;
    
    @Before
    public void setup() {
        builder = new StateBlockBuilder(
                NanoAccount.parseAddress("nano_3dmtrrws3pocycmbqwawk6xs7446qxa36fcncush4s1pejk16ksbmakis78m"),
                "90204CCDFB3E7B15F5AA79B4DED8E7268826853231B67B2C16DB37559D578488",
                NanoAccount.parseAddress("nano_34prihdxwz3u4ps8qjnn14p7ujyewkoxkwyxm3u665it8rg5rdqw84qrypzk"),
                new BigInteger("1234567"));
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
        StateBlockBuilder sb = TestConstants.randStateBlock();
        BlockIntent intent;
        
        // Test receive
        intent = sb.setSubtype(StateBlockSubType.RECEIVE).build().getIntent();
        assertEquals(BlockIntent.UncertainBool.UNKNOWN, intent.isChangeRep());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isEpochUpgrade());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isFirstBlock());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isReceiveFunds());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isSendFunds());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isTransactional());
        assertEquals(BlockIntent.UncertainBool.UNKNOWN, intent.isSpecial());
        
        // Test send
        intent = sb.setSubtype(StateBlockSubType.SEND).build().getIntent();
        assertEquals(BlockIntent.UncertainBool.UNKNOWN, intent.isChangeRep());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isEpochUpgrade());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isFirstBlock());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isReceiveFunds());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isSendFunds());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isTransactional());
        assertEquals(BlockIntent.UncertainBool.UNKNOWN, intent.isSpecial());
    
        // Test open (with OPEN subtype)
        intent = sb.setSubtype(StateBlockSubType.OPEN).build().getIntent();
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isChangeRep());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isEpochUpgrade());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isFirstBlock());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isReceiveFunds());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isSendFunds());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isTransactional());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isSpecial());
        
        // Test change
        intent = sb.setSubtype(StateBlockSubType.CHANGE).build().getIntent();
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isChangeRep());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isEpochUpgrade());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isFirstBlock());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isReceiveFunds());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isSendFunds());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isTransactional());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isSpecial());
        
        // Test epoch
        intent = sb.setSubtype(StateBlockSubType.EPOCH).build().getIntent();
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isChangeRep());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isEpochUpgrade());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isFirstBlock());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isReceiveFunds());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isSendFunds());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isTransactional());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isSpecial());
    
        // Test unknown
        intent = sb.setSubtype(null).build().getIntent();
        assertEquals(BlockIntent.UncertainBool.UNKNOWN, intent.isChangeRep());
        assertEquals(BlockIntent.UncertainBool.UNKNOWN, intent.isEpochUpgrade());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isFirstBlock());
        assertEquals(BlockIntent.UncertainBool.UNKNOWN, intent.isReceiveFunds());
        assertEquals(BlockIntent.UncertainBool.UNKNOWN, intent.isSendFunds());
        assertEquals(BlockIntent.UncertainBool.UNKNOWN, intent.isTransactional());
        assertEquals(BlockIntent.UncertainBool.UNKNOWN, intent.isSpecial());
    
        // Test unknown (with blank previous hash, expecting OPEN)
        intent = sb.setSubtype(null).setPreviousBlockHash(JNH.ZEROES_64).build().getIntent();
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isChangeRep());
        assertEquals(BlockIntent.UncertainBool.UNKNOWN, intent.isEpochUpgrade());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isFirstBlock());
        assertEquals(BlockIntent.UncertainBool.UNKNOWN, intent.isReceiveFunds());
        assertEquals(BlockIntent.UncertainBool.UNKNOWN, intent.isSendFunds());
        assertEquals(BlockIntent.UncertainBool.UNKNOWN, intent.isTransactional());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isSpecial());
    }
    
}