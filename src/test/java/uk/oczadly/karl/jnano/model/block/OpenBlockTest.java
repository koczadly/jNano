package uk.oczadly.karl.jnano.model.block;

import org.junit.Test;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import static org.junit.Assert.assertEquals;

public class OpenBlockTest {
    
    final OpenBlock TEST_BLOCK = new OpenBlock(null,
            "9F0C933C8ADE004D808EA1985FA746A7E95BA2A38F867640F53EC8F180BDFE9E2C1268DEAD7C2664F356E37ABA36" +
                    "2BC58E46DBA03E523A7B5A19E4B6EB12BB02",
            new WorkSolution("62f05417dd3fb691"),
            "991CF190094C00F0B68E2E5F75F6BEE95A2E0BD93CEAA4A6734DB9F19B728948",
            NanoAccount.parseAddress("nano_3robocazheuxet5ju1gtif4cefkhfbupkykc97hfanof859ie9ajpdfhy3ez"),
            NanoAccount.parseAddress("nano_31xitw55kb3ko8yaz3439hqaqpibxa9shx76suaa3no786do3hjuz8dy6izw"));
    
    
    @Test
    public void testHashing() {
        assertEquals("C62EA8D1AEFE50328CE8C06E279E6AC36038B751C574F10BCB8C7AEA6C933694", TEST_BLOCK.getHash());
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
    
}