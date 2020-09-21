/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import org.junit.Test;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SendBlockTest {
    
    final String TB_SIGNATURE = "DEE5EC5D771E92B5DE3C76EBFE8FE844284A3AFCA32D6183643BA1B553C75F2377A1138DB620832D6557" +
            "C0AD46D804D577F372C592C1D05DDA10571D14872304";
    final WorkSolution TB_WORK = new WorkSolution("8017b5301c0b822c");
    final String TB_PREVIOUS = "91862D068AB5F836360738002EBB421B0A89996CF1AF64E1C9D400B2410BEDF0";
    final NanoAccount TB_DESTINATION = NanoAccount.parseAddress(
            "nano_14ghgrw1y9itsopkyscpofbpgrjr6hoy1qug4gda3utunrq11xzee9yfhtg3");
    final NanoAmount TB_BALANCE = new NanoAmount("689999000000000000000000000000");
    
    final SendBlock TEST_BLOCK = new SendBlock(TB_SIGNATURE, TB_WORK, TB_PREVIOUS, TB_DESTINATION, TB_BALANCE);
    
    final String TEST_BLOCK_JSON = "{\n" +
            "    \"type\": \"send\",\n" +
            "    \"previous\": \"91862D068AB5F836360738002EBB421B0A89996CF1AF64E1C9D400B2410BEDF0\",\n" +
            "    \"destination\": \"nano_14ghgrw1y9itsopkyscpofbpgrjr6hoy1qug4gda3utunrq11xzee9yfhtg3\",\n" +
            "    \"balance\": \"689999000000000000000000000000\",\n" +
            "    \"work\": \"8017b5301c0b822c\",\n" +
            "    \"signature\": \"DEE5EC5D771E92B5DE3C76EBFE8FE844284A3AFCA32D6183643BA1B553C75F2377A1138DB620832D65" +
            "57C0AD46D804D577F372C592C1D05DDA10571D14872304\"\n" +
            "  }";
    
    
    @Test
    public void testParse() {
        SendBlock block = SendBlock.parse(TEST_BLOCK_JSON);
        assertEquals(block, TEST_BLOCK);
        assertEquals(TB_SIGNATURE, block.getSignature());
        assertEquals(TB_WORK, block.getWorkSolution());
        assertEquals(TB_PREVIOUS, block.getPreviousBlockHash());
        assertEquals(TB_BALANCE, block.getBalance());
        assertEquals(TB_DESTINATION, block.getDestinationAccount());
    }
    
    @Test
    public void testEquality() {
        SendBlock b1 = SendBlock.parse(TEST_BLOCK_JSON);
        SendBlock b2 = SendBlock.parse(TEST_BLOCK_JSON);
        assertEquals(b1, b2);
        assertTrue(b1.contentEquals(b2));
        assertTrue(b2.contentEquals(b1));
    }
    
    @Test
    public void testHashing() {
        assertEquals("8DAA2C593B4D1D0EA44DF7A84C91167E991D7EC6E08333CDCD7B7082B68B1E08", TEST_BLOCK.getHash());
    }
    
    @Test
    public void testIntent() {
        BlockIntent intent = TEST_BLOCK.getIntent();
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isChangeRep());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isEpochUpgrade());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isFirstBlock());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isReceiveFunds());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isSendFunds());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isGenesis());
        assertEquals(BlockIntent.UncertainBool.TRUE, intent.isTransactional());
        assertEquals(BlockIntent.UncertainBool.FALSE, intent.isSpecial());
    }
    
}