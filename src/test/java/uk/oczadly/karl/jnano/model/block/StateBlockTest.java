/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import org.junit.Test;
import uk.oczadly.karl.jnano.TestConstants;
import uk.oczadly.karl.jnano.internal.JNC;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.epoch.UnrecognizedEpochException;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.util.NetworkConstants;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class StateBlockTest {
    
    static final HexData TB_PRIVKEY = new HexData("8977C62F7D02E3FEA690BA90EB09F750B9BB8C3FB5FC61570C018E2D62E9FCF6");
    static final HexData TB_SIGNATURE = new HexData("187AD5FCC225248B160AEFF0DC28AF598B94F5B7E0ABED474A5A4EE3CD82018" +
            "845FA9624B5B0BF53D28251024FE563B95CD58610DF735D68510820E8B428F203");
    static final WorkSolution TB_WORK = new WorkSolution("508bc946fe6d22e7");
    static final NanoAccount TB_ACCOUNT =
            NanoAccount.parseAddress("nano_3wznjw17oqwugahd93rj4suhjfh3f17skp9egp7suzpqgsyxfocxxqg4n7wt");
    static final NanoAccount TB_REP =
            NanoAccount.parseAddress("nano_34prihdxwz3u4ps8qjnn14p7ujyewkoxkwyxm3u665it8rg5rdqw84qrypzk");
    static final HexData TB_PREVIOUS = new HexData("90204CCDFB3E7B15F5AA79B4DED8E7268826853231B67B2C16DB37559D578488");
    static final HexData TB_LINK = new HexData("65706F636820763220626C6F636B000000000000000000000000000000000000");
    static final NanoAmount TB_BALANCE = NanoAmount.valueOfRaw("1234567");
    static final StateBlockSubType TB_SUBTYPE = StateBlockSubType.SEND;
    
    static final StateBlockBuilder TEST_BUILDER = new StateBlockBuilder()
            .signature(TB_SIGNATURE)
            .work(TB_WORK)
            .subtype(TB_SUBTYPE)
            .account(TB_ACCOUNT)
            .representative(TB_REP)
            .previous(TB_PREVIOUS)
            .balance(TB_BALANCE)
            .link(TB_LINK);
    
    static final String TEST_BLOCK_JSON = "{\n" +
            "    \"type\": \"state\",\n" +
            "    \"account\": \"nano_3wznjw17oqwugahd93rj4suhjfh3f17skp9egp7suzpqgsyxfocxxqg4n7wt\",\n" +
            "    \"previous\": \"90204CCDFB3E7B15F5AA79B4DED8E7268826853231B67B2C16DB37559D578488\",\n" +
            "    \"representative\": \"nano_34prihdxwz3u4ps8qjnn14p7ujyewkoxkwyxm3u665it8rg5rdqw84qrypzk\",\n" +
            "    \"balance\": \"1234567\",\n" +
            "    \"link\": \"65706F636820763220626C6F636B000000000000000000000000000000000000\",\n" +
            "    \"link_as_account\": \"nano_1sdifxjpia5p8ai86u5hefoi1111111111111111111111111111ngspq7ps\",\n" +
            "    \"signature\": \"187AD5FCC225248B160AEFF0DC28AF598B94F5B7E0ABED474A5A4EE3CD82018845FA9624B5B0BF53D" +
            "28251024FE563B95CD58610DF735D68510820E8B428F203\",\n" +
            "    \"work\": \"508bc946fe6d22e7\",\n" +
            "    \"subtype\": \"send\"\n" +
            "  }";
    
    static final StateBlock TEST_BLOCK = TEST_BUILDER.build();
    
    
    static StateBlockBuilder builder() {
        return new StateBlockBuilder(TEST_BUILDER);
    }
    
    
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
        assertEquals(TB_LINK, block.getLink().asHex());
    }
    
    @Test
    public void testEquality() {
        StateBlock block1 = builder().build();
        StateBlock block2 = builder().build();
        StateBlock block3 = builder().subtype(StateBlockSubType.RECEIVE).build();
        StateBlock block4 = builder().removeSignature()
                .link("62204CCDFB3E7B15F5AA79B4DED8E7268826853231B67B2C16DB37559D578488")
                .build();
    
        // Equal
        assertEquals(block1, block2);
        assertEquals(block1, block3);
        assertTrue(block1.contentEquals(block2));
        assertTrue(block2.contentEquals(block1));
        // Not equal
        assertNotEquals(block1, block4);
        assertFalse(block1.contentEquals(block3));
        assertFalse(block3.contentEquals(block1));
        assertFalse(block4.contentEquals(block1));
        // Check hashcode
        assertEquals(block1.hashCode(), block2.hashCode());
    }
    
    @Test
    public void testHashing() {
        assertEquals("FDBE6C7E2272C17AE12E1E322902F2E8420351272ACD9A23BDD3B7839AAFA008",
                TEST_BLOCK.getHash().toString());
    }
    
    @Test
    public void testSigning() {
        StateBlock b = builder()
                .signature((HexData)null)
                .build();
        assertNull(b.getSignature());
        b.sign(TB_PRIVKEY);
        assertEquals(TB_SIGNATURE, b.getSignature());
    }
    
    @Test
    public void testSigVerification() {
        assertTrue(TEST_BLOCK.verifySignature(TB_ACCOUNT));
        assertFalse(TEST_BLOCK.verifySignature(NanoAccount.ZERO_ACCOUNT));
    }
    
    @Test
    public void testEpochSigVerification() {
        // V2 epoch block
        StateBlock block = StateBlock.parse("{\n" +
                "\"type\": \"state\",\n" +
                "\"account\": \"nano_3x4ui45q1cw8hydmfdn4ec5ijsdqi4ryp14g4ayh71jcdkwmddrq7ca9xzn9\",\n" +
                "\"previous\": \"D7B1B764399B3417BC1220C602A9608D9C883CF2064EA481E14152813F3A6B9E\",\n" +
                "\"representative\": \"nano_3rw4un6ys57hrb39sy1qx8qy5wukst1iiponztrz9qiz6qqa55kxzx4491or\",\n" +
                "\"balance\": \"0\",\n" +
                "\"link\": \"65706F636820763220626C6F636B000000000000000000000000000000000000\",\n" +
                "\"link_as_account\": \"nano_1sdifxjpia5p8ai86u5hefoi1111111111111111111111111111ngspq7ps\",\n" +
                "\"signature\": \"C79A2779903119007A5A597EBA57931485D729CB4C5D12502967C3645624C042D6E867D6E783CFF7D2" +
                "B01292AB8834A66BD7F9508B2981FEBF14542988F8AF02\",\n" +
                "\"work\": \"1c147cfad9657bb5\",\n" +
                "\"subtype\": \"epoch\"}");
        
        assertTrue(block.verifySignature());
        assertFalse(block.verifySignature(block.getAccount()));
        assertThrows(UnrecognizedEpochException.class,
                () -> block.verifySignature(NetworkConstants.BANANO.getEpochUpgrades()));
    }
    
    @Test
    public void testSelfSignedVerification() {
        // Standard block
        assertTrue(TEST_BLOCK.verifySignature());
        
        // Standard block incorrect sig
        StateBlock incorrectSig = builder().build();
        incorrectSig.setSignature(JNC.ZEROES_128_HD);
        assertFalse(incorrectSig.verifySignature());
        
        // Epoch
        StateBlock epoch = StateBlock.parse("{\n" +
                "    \"type\": \"state\",\n" +
                "    \"account\": \"nano_3x4ui45q1cw8hydmfdn4ec5ijsdqi4ryp14g4ayh71jcdkwmddrq7ca9xzn9\",\n" +
                "    \"previous\": \"D7B1B764399B3417BC1220C602A9608D9C883CF2064EA481E14152813F3A6B9E\",\n" +
                "    \"representative\": \"nano_3rw4un6ys57hrb39sy1qx8qy5wukst1iiponztrz9qiz6qqa55kxzx4491or\",\n" +
                "    \"balance\": \"0\",\n" +
                "    \"link\": \"65706F636820763220626C6F636B000000000000000000000000000000000000\",\n" +
                "    \"signature\": \"C79A2779903119007A5A597EBA57931485D729CB4C5D12502967C3645624C042D6E867D6E783CFF" +
                "7D2B01292AB8834A66BD7F9508B2981FEBF14542988F8AF02\",\n" +
                "    \"work\": \"1c147cfad9657bb5\",\n" +
                "    \"subtype\": \"epoch\"\n" +
                "  }");
        assertTrue(epoch.verifySignature());
        
        // Epoch with incorrect sig
        epoch.setSignature(JNC.ZEROES_128_HD);
        assertFalse(epoch.verifySignature());
    }
    
    @Test
    public void testLink() {
        // Send
        assertEquals(LinkData.Intent.DESTINATION_ACCOUNT,
                TestConstants.randStateBlock()
                        .subtype(StateBlockSubType.SEND)
                        .build().getLink().getIntent());
        // Receive
        assertEquals(LinkData.Intent.SOURCE_HASH,
                TestConstants.randStateBlock()
                        .subtype(StateBlockSubType.RECEIVE)
                        .build().getLink().getIntent());
        // Epoch
        assertEquals(LinkData.Intent.EPOCH_IDENTIFIER,
                TestConstants.randStateBlock()
                        .subtype(StateBlockSubType.EPOCH)
                        .build().getLink().getIntent());
        // Change
        assertEquals(LinkData.Intent.UNUSED,
                TestConstants.randStateBlock()
                        .subtype(StateBlockSubType.CHANGE)
                        .build().getLink().getIntent());
    }
    
    @Test
    public void testIntent() {
        BlockIntent intent;
        
        // Test receive
        intent = TestConstants.randStateBlock()
                .subtype(StateBlockSubType.RECEIVE)
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
                .subtype(StateBlockSubType.SEND)
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
                .subtype(StateBlockSubType.OPEN)
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
                .subtype(StateBlockSubType.CHANGE)
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
                .subtype(StateBlockSubType.EPOCH)
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
                .subtype(StateBlockSubType.OPEN)
                .previous(JNC.ZEROES_64)
                .balance(BigInteger.ZERO)
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
    public void testToJson() {
        JsonObject expected = JNH.parseJson(TEST_BLOCK_JSON);
        assertEquals(expected, TEST_BLOCK.toJsonObject());
        assertEquals(expected, JNH.parseJson(TEST_BLOCK.toJsonString()));
    }
    
}