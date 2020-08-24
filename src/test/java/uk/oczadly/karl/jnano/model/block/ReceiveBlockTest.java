package uk.oczadly.karl.jnano.model.block;

import org.junit.Test;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import static org.junit.Assert.assertEquals;

public class ReceiveBlockTest {
    
    @Test
    public void testHashing() {
        ReceiveBlock b = new ReceiveBlock(null,
                "9F0C933C8ADE004D808EA1985FA746A7E95BA2A38F867640F53EC8F180BDFE9E2C1268DEAD7C2664F356E37ABA36" +
                        "2BC58E46DBA03E523A7B5A19E4B6EB12BB02",
                new WorkSolution("62f05417dd3fb691"),
                "991CF190094C00F0B68E2E5F75F6BEE95A2E0BD93CEAA4A6734DB9F19B728948",
                "191CF190094C00F0B68E2E5F75F6BEE95A2E0BD93CEAA4A6734DB9F19B728948");
        
        assertEquals("A9F2260E1348A0922C8417A6CB2F36A90C11D7F52B24C514D22AEAD367BB1F01", b.getHash());
    }
    
}