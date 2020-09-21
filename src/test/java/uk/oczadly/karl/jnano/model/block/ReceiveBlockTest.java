/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import org.junit.Test;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReceiveBlockTest {
    
    final String TB_SIGNATURE = "4A7BC7E8B48BFBA5ECA937511DAE541EB4672097EE3C1DB1BDE398902046057BB69B43638EC0BF25745" +
            "F6C6D825D90264A9297CAFFA0CDB32AD3728B5A5B6507";
    final WorkSolution TB_WORK = new WorkSolution("6671aa4926656605");
    final String TB_SOURCE = "8876A4CB68DBFF02C79EC6B1E1F77574FCD86808184DEC69515AE25204FEE94A";
    final String TB_PREVIOUS = "8DAA2C593B4D1D0EA44DF7A84C91167E991D7EC6E08333CDCD7B7082B68B1E08";
    
    final ReceiveBlock TEST_BLOCK = new ReceiveBlock(TB_SIGNATURE, TB_WORK, TB_PREVIOUS, TB_SOURCE);
    
    final String TEST_BLOCK_JSON = "{\n" +
            "    \"type\": \"receive\",\n" +
            "    \"previous\": \"8DAA2C593B4D1D0EA44DF7A84C91167E991D7EC6E08333CDCD7B7082B68B1E08\",\n" +
            "    \"source\": \"8876A4CB68DBFF02C79EC6B1E1F77574FCD86808184DEC69515AE25204FEE94A\",\n" +
            "    \"work\": \"6671aa4926656605\",\n" +
            "    \"signature\": \"4A7BC7E8B48BFBA5ECA937511DAE541EB4672097EE3C1DB1BDE398902046057BB69B43638EC0BF25745" +
            "F6C6D825D90264A9297CAFFA0CDB32AD3728B5A5B6507\"\n" +
            "  }";
    
    
    @Test
    public void testParse() {
        ReceiveBlock block = ReceiveBlock.parse(TEST_BLOCK_JSON);
        assertEquals(block, TEST_BLOCK);
        assertEquals(TB_SIGNATURE, block.getSignature());
        assertEquals(TB_WORK, block.getWorkSolution());
        assertEquals(TB_SOURCE, block.getSourceBlockHash());
        assertEquals(TB_PREVIOUS, block.getPreviousBlockHash());
    }
    
    @Test
    public void testEquality() {
        ReceiveBlock b1 = ReceiveBlock.parse(TEST_BLOCK_JSON);
        ReceiveBlock b2 = ReceiveBlock.parse(TEST_BLOCK_JSON);
        assertEquals(b1, b2);
        assertTrue(b1.contentEquals(b2));
        assertTrue(b2.contentEquals(b1));
    }
    
    @Test
    public void testHashing() {
        assertEquals("E4DED3970463EE415F70269CC10722473B1B381FA55CFF4D738FDDC32EF7B62D", TEST_BLOCK.getHash());
    }
    
    @Test
    public void testIntent() {
        BlockIntent intent = TEST_BLOCK.getIntent();
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isChangeRep());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isEpochUpgrade());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isFirstBlock());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isReceiveFunds());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isSendFunds());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isGenesis());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isTransactional());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isSpecial());
    }
    
}