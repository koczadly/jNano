/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util;

import org.junit.Test;
import uk.oczadly.karl.jnano.model.NanoAccount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NanoConstantsTest {
    
    @Test
    public void testLiveNet() {
        NetworkConstants net = NanoConstants.NANO_LIVE_NET;
    
        assertEquals("991CF190094C00F0B68E2E5F75F6BEE95A2E0BD93CEAA4A6734DB9F19B728948",
                net.getNetworkIdentifier());
        assertEquals("991CF190094C00F0B68E2E5F75F6BEE95A2E0BD93CEAA4A6734DB9F19B728948",
                net.getGenesisBlock().getHash());
        assertEquals("nano", net.getAddressPrefix());
        assertEquals(NanoAccount.parseAddress("nano_1111111111111111111111111111111111111111111111111111hifc8npp"),
                net.getBurnAddress());
    }
    
    @Test
    public void testFromHash() {
        assertEquals(NanoConstants.NANO_LIVE_NET, NanoConstants.getNetworkFromGenesisHash(
                "991CF190094C00F0B68E2E5F75F6BEE95A2E0BD93CEAA4A6734DB9F19B728948"));
        assertEquals(NanoConstants.BANANO_LIVE_NET, NanoConstants.getNetworkFromGenesisHash(
                "F61A79F286ABC5CC01D3D09686F0567812B889A5C63ADE0E82DD30F3B2D96463"));
        assertNull(NanoConstants.getNetworkFromGenesisHash(
                "A61A79F286ABC5CC01D3D09686F0567812B889A5C63ADE0E82DD30F3B2D96463"));
    }
    
}