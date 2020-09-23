/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import org.junit.Test;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OpenBlockTest {
    
    final String TB_SIGNATURE = "5DBA22E6E2294C46BED780CD6428576290890F8F40C58E4456B2AD330DCB0EB16E178919635659477C8" +
            "CDEF1C49E679B9AB02E2F587675D086653A21EE16D304";
    final WorkSolution TB_WORK = new WorkSolution("35a4c486e395053a");
    final String TB_SOURCE = "A88DE9C3B82C09402F96AC3B5678EE64519A243E089B7284207689D829185169";
    final NanoAccount TB_ACCOUNT = NanoAccount.parseAddress(
            "nano_1asau6gr8ft5ykynpkauctrq1w37sdasdymuigtxotim6kxoa3rgn3dpenis");
    final NanoAccount TB_REP = NanoAccount.parseAddress(
            "nano_3rw4un6ys57hrb39sy1qx8qy5wukst1iiponztrz9qiz6qqa55kxzx4491or");
    
    final OpenBlock TEST_BLOCK = new OpenBlock(TB_SIGNATURE, TB_WORK, TB_SOURCE, TB_ACCOUNT, TB_REP);
    
    final String TEST_BLOCK_JSON = "{\n" +
            "    \"type\": \"open\",\n" +
            "    \"source\": \"A88DE9C3B82C09402F96AC3B5678EE64519A243E089B7284207689D829185169\",\n" +
            "    \"representative\": \"nano_3rw4un6ys57hrb39sy1qx8qy5wukst1iiponztrz9qiz6qqa55kxzx4491or\",\n" +
            "    \"account\": \"nano_1asau6gr8ft5ykynpkauctrq1w37sdasdymuigtxotim6kxoa3rgn3dpenis\",\n" +
            "    \"work\": \"35a4c486e395053a\",\n" +
            "    \"signature\": \"5DBA22E6E2294C46BED780CD6428576290890F8F40C58E4456B2AD330DCB0EB16E178919635659477" +
            "C8CDEF1C49E679B9AB02E2F587675D086653A21EE16D304\"\n" +
            "  }";
    
    
    @Test
    public void testParse() {
        OpenBlock block = OpenBlock.parse(TEST_BLOCK_JSON);
        assertEquals(block, TEST_BLOCK);
        assertEquals(TB_SIGNATURE, block.getSignature());
        assertEquals(TB_WORK, block.getWorkSolution());
        assertEquals(TB_SOURCE, block.getSourceBlockHash());
        assertEquals(TB_ACCOUNT, block.getAccount());
        assertEquals(TB_REP, block.getRepresentative());
    }
    
    @Test
    public void testEquality() {
        OpenBlock b1 = OpenBlock.parse(TEST_BLOCK_JSON);
        OpenBlock b2 = OpenBlock.parse(TEST_BLOCK_JSON);
        assertEquals(b1, b2);
        assertTrue(b1.contentEquals(b2));
        assertTrue(b2.contentEquals(b1));
    }
    
    @Test
    public void testHashing() {
        assertEquals("279C791486FF3796BDEDF1B85A5349C172B4892F50BA22016C44F836BDC95993", TEST_BLOCK.getHash());
    }
    
    @Test
    public void testIntent() {
        BlockIntent intent = TEST_BLOCK.getIntent();
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isChangeRep());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isEpochUpgrade());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isFirstBlock());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isReceiveFunds());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isSendFunds());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isTransactional());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isSpecial());
    }
    
    @Test
    public void testToJson() {
        JsonObject expected = JNH.parseJson(TEST_BLOCK_JSON);
        assertEquals(expected, TEST_BLOCK.toJsonObject());
        assertEquals(expected, JNH.parseJson(TEST_BLOCK.toJsonString()));
    }
    
}