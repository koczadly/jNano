package uk.oczadly.karl.jnano.model.block;

import org.junit.Test;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import static org.junit.Assert.assertEquals;

public class ChangeBlockTest {
    
    @Test
    public void testHashing() {
        ChangeBlock b = new ChangeBlock(null,
                "9F0C933C8ADE004D808EA1985FA746A7E95BA2A38F867640F53EC8F180BDFE9E2C1268DEAD7C2664F356E37ABA36" +
                        "2BC58E46DBA03E523A7B5A19E4B6EB12BB02",
                new WorkSolution("62f05417dd3fb691"),
                "991CF190094C00F0B68E2E5F75F6BEE95A2E0BD93CEAA4A6734DB9F19B728948",
                NanoAccount.parseAddress("nano_3robocazheuxet5ju1gtif4cefkhfbupkykc97hfanof859ie9ajpdfhy3ez"));
        
        assertEquals("1D8B97BD8BD4F06FF46CA158A079C2E92CDC27EB6A95AE0B6A4F136564FB5F62", b.getHash());
    }
    
}