package uk.oczadly.karl.jnano.util;

import org.junit.Test;
import uk.oczadly.karl.jnano.model.NanoAccount;

import static org.junit.Assert.assertEquals;

public class NanoConstantsTest {
    
    @Test
    public void testLiveNet() {
        NetworkConstants net = NanoConstants.NANO_LIVE_NET;
        
        assertEquals("991CF190094C00F0B68E2E5F75F6BEE95A2E0BD93CEAA4A6734DB9F19B728948",
                net.getGenesisBlock().getHash());
        assertEquals("nano", net.getAddressPrefix());
        assertEquals(NanoAccount.parse("nano_1111111111111111111111111111111111111111111111111111hifc8npp"),
                net.getBurnAddress());
    }
    
}