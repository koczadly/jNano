package uk.oczadly.karl.jnano.util;

import uk.oczadly.karl.jnano.model.NanoAccount;

public final class NanoConstants {
    private NanoConstants() {}
    
    
    /**
     * The genesis account's address on the live Nano network.
     */
    public static final NanoAccount ADDRESS_GENESIS_LIVE =
            NanoAccount.parseSegment("3t6k35gi95xu6tergt6p69ck76ogmitsa8mnijtpxm9fkcm736xt");
    
    /**
     * The official designated burn address. Funds sent to this address will be permanently irretrievable.
     */
    public static final NanoAccount ADDRESS_BURN =
            NanoAccount.parseSegment("1111111111111111111111111111111111111111111111111111");
    
    
    /**
     * The block hash of the genesis transaction on the live Nano network. This represents the block/transaction where
     * all Nano units were initially introduced to the network.
     */
    public static final String BLOCK_HASH_GENESIS = "991CF190094C00F0B68E2E5F75F6BEE95A2E0BD93CEAA4A6734DB9F19B728948";

}
