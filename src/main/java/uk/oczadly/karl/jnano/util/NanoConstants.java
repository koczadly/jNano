package uk.oczadly.karl.jnano.util;

public final class NanoConstants {
    private NanoConstants() {}
    
    
    /**
     * The genesis account's address on the live Nano network.
     */
    public static final String ADDRESS_GENESIS_LIVE =
            "nano_3t6k35gi95xu6tergt6p69ck76ogmitsa8mnijtpxm9fkcm736xtoncuohr3";
    
    /**
     * The official designated burn address. Funds sent to this address will be permanently irretrievable.
     */
    public static final String ADDRESS_BURN = "nano_1111111111111111111111111111111111111111111111111111hifc8npp";
    
    
    /**
     * The block hash of the genesis transaction on the live Nano network. This represents the block/transaction where
     * all Nano units were initially introduced to the network.
     */
    public static final String BLOCK_HASH_GENESIS = "991CF190094C00F0B68E2E5F75F6BEE95A2E0BD93CEAA4A6734DB9F19B728948";
    
    /**
     * The {@code link} data value for V1 epoch blocks. This epoch represents the transition from legacy
     * blocks (send/receive/open/change) to UTX state blocks.
     */
    public static final String EPOCH_IDENTIFIER_V1 = "65706F636820763120626C6F636B000000000000000000000000000000000000";
    
    /**
     * The {@code link} data value for V2 epoch blocks. This epoch represents the change in required work difficulties.
     */
    public static final String EPOCH_IDENTIFIER_V2 = "65706F636820763220626C6F636B000000000000000000000000000000000000";

}
