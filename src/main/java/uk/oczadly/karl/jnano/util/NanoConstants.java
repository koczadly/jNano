package uk.oczadly.karl.jnano.util;

import uk.oczadly.karl.jnano.internal.JNanoHelper;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.OpenBlock;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

/**
 * This class contains a set of static constant values. Any values which represent a state of the network are for
 * the official live Nano network only.
 */
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
    public static final NanoAccount ADDRESS_BURN = NanoAccount.parsePublicKey(JNanoHelper.ZEROES_64);
    
    
    /**
     * The genesis block on the live Nano network. This represents the block/transaction where all Nano units were
     * initially introduced to the network.
     */
    @SuppressWarnings("deprecation")
    public static final Block BLOCK_GENESIS_LIVE = new OpenBlock(
            "9F0C933C8ADE004D808EA1985FA746A7E95BA2A38F867640F53EC8F180BDFE9E2C1268DEAD7C2664F356E37ABA362BC" +
                    "58E46DBA03E523A7B5A19E4B6EB12BB02",
            new WorkSolution("62f05417dd3fb691"),
            "E89208DD038FBB269987689621D52292AE9C35941A7484756ECCED92A65093BA",
            ADDRESS_GENESIS_LIVE, ADDRESS_GENESIS_LIVE);
    
    /**
     * The block hash of the genesis transaction on the live Nano network. This represents the block/transaction where
     * all Nano units were initially introduced to the network.
     * @deprecated Use the {@link Block#getHash()} method of {@link #BLOCK_GENESIS_LIVE}.
     */
    @Deprecated(forRemoval = true)
    public static final String BLOCK_HASH_GENESIS = BLOCK_GENESIS_LIVE.getHash();
    
}
