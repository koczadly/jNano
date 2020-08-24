package uk.oczadly.karl.jnano.model.block;

import org.junit.Test;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class SendBlockTest {
    
    @Test
    public void testHashing() {
        SendBlock b = new SendBlock(null,
                "9F0C933C8ADE004D808EA1985FA746A7E95BA2A38F867640F53EC8F180BDFE9E2C1268DEAD7C2664F356E37ABA36" +
                        "2BC58E46DBA03E523A7B5A19E4B6EB12BB02",
                new WorkSolution("62f05417dd3fb691"),
                "991CF190094C00F0B68E2E5F75F6BEE95A2E0BD93CEAA4A6734DB9F19B728948",
                NanoAccount.parseAddress("nano_3robocazheuxet5ju1gtif4cefkhfbupkykc97hfanof859ie9ajpdfhy3ez"),
                new BigInteger("1234567"));
        
        assertEquals("1DB1218A2FE2CB023E93FFDA68F6DB04EB18F6B84D905D6442EE51B3E99B4CB7", b.getHash());
    }
    
}