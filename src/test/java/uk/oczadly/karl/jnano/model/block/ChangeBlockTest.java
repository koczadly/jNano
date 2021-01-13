/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import org.junit.Test;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.util.NanoConstants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChangeBlockTest {
    
    final HexData TB_SIGNATURE = new HexData("1D1A887AECC4A1C581D5CFC64E60B1AFAA6F820EB60FB581FE23D0A2C8AD5DC948311" +
            "F2E872BC83B6D253BDA7FEC4905E0E724006A7E2E4F5A2A6CB671695B09");
    final WorkSolution TB_WORK = new WorkSolution("9bbddcc234c0e623");
    final HexData TB_PREVIOUS = new HexData("279C791486FF3796BDEDF1B85A5349C172B4892F50BA22016C44F836BDC95993");
    final NanoAccount TB_REP = NanoAccount.parseAddress(
            "nano_1asau6gr8ft5ykynpkauctrq1w37sdasdymuigtxotim6kxoa3rgn3dpenis");
    
    final ChangeBlock TEST_BLOCK = new ChangeBlock(TB_SIGNATURE, TB_WORK, TB_PREVIOUS, TB_REP);
    
    final String TEST_BLOCK_JSON = "{\n" +
            "    \"type\": \"change\",\n" +
            "    \"previous\": \"279C791486FF3796BDEDF1B85A5349C172B4892F50BA22016C44F836BDC95993\",\n" +
            "    \"representative\": \"nano_1asau6gr8ft5ykynpkauctrq1w37sdasdymuigtxotim6kxoa3rgn3dpenis\",\n" +
            "    \"work\": \"9bbddcc234c0e623\",\n" +
            "    \"signature\": \"1D1A887AECC4A1C581D5CFC64E60B1AFAA6F820EB60FB581FE23D0A2C8AD5DC948311F2E872BC83B6D2" +
            "53BDA7FEC4905E0E724006A7E2E4F5A2A6CB671695B09\"\n" +
            "  }";
    
    
    @Test
    public void testParse() {
        ChangeBlock block = ChangeBlock.parse(TEST_BLOCK_JSON);
        assertEquals(block, TEST_BLOCK);
        assertEquals(TB_SIGNATURE, block.getSignature());
        assertEquals(TB_WORK, block.getWorkSolution());
        assertEquals(TB_PREVIOUS, block.getPrevHash());
        assertEquals(TB_REP, block.getRepresentative());
    }
    
    @Test
    public void testEquality() {
        ChangeBlock b1 = ChangeBlock.parse(TEST_BLOCK_JSON);
        ChangeBlock b2 = ChangeBlock.parse(TEST_BLOCK_JSON);
        assertEquals(b1, b2);
        assertTrue(b1.contentEquals(b2));
        assertTrue(b2.contentEquals(b1));
    }
    
    @Test
    public void testHashing() {
        assertEquals("91862D068AB5F836360738002EBB421B0A89996CF1AF64E1C9D400B2410BEDF0",
                TEST_BLOCK.getHash().toHexString());
    }
    
    @Test
    public void testIntent() {
        // Standard
        BlockIntent intent = TEST_BLOCK.getIntent();
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isChangeRep());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isEpochUpgrade());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isFirstBlock());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isReceiveFunds());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isSendFunds());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isGenesis());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isTransactional());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isSpecial());
        
        // Genesis
        assertEquals(BlockIntent.UncertainBool.TRUE,
                NanoConstants.NANO_LIVE_NET.getGenesisBlock().getIntent().isGenesis());
    }
    
    @Test
    public void testToJson() {
        JsonObject expected = JNH.parseJson(TEST_BLOCK_JSON);
        assertEquals(expected, TEST_BLOCK.toJsonObject());
        assertEquals(expected, JNH.parseJson(TEST_BLOCK.toJsonString()));
    }
    
}