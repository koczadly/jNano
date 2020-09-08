/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util;

import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.math.BigInteger;

/**
 * This class contains a set of static constant values. Any values which represent a state of the network are for
 * the official live Nano network only.
 */
public final class NanoConstants {
    private NanoConstants() {}
    
    
    /**
     * The maximum possible balance value, in raw units. This does not account for burned or undistributed funds, but
     * is simply the number of units which are generated within the genesis block.
     */
    public static final BigInteger MAX_BALANCE_RAW = JNH.BIGINT_MAX_128;
    
    /**
     * Constants representing the official live Nano network.
     * @see <a href="https://nano.org">Nano official website</a>
     */
    public static final NetworkConstants NANO_LIVE_NET = new NetworkConstants(
            "Nano live network",
            NanoAccount.DEFAULT_PREFIX,
            "1111111111111111111111111111111111111111111111111111",
            "9F0C933C8ADE004D808EA1985FA746A7E95BA2A38F867640F53EC8F180BDFE9E2C1268DEAD7C2664F356E37ABA362BC58E46D" +
                    "BA03E523A7B5A19E4B6EB12BB02",
            new WorkSolution("62f05417dd3fb691"),
            "3t6k35gi95xu6tergt6p69ck76ogmitsa8mnijtpxm9fkcm736xt",
            new NetworkConstants.WorkDifficultiesV2(
                    0xfffffff800000000L, 0xfffffe0000000000L));
    
    /**
     * Constants representing the official beta Nano network.
     * @see <a href="https://beta.nano.org">Nano beta website</a>
     */
    public static final NetworkConstants NANO_BETA_NET = new NetworkConstants(
            "Nano beta network",
            NanoAccount.DEFAULT_PREFIX,
            "1111111111111111111111111111111111111111111111111111",
            "DB9EFAC98A28EEA048E722F91C2A2720E1D8EF2A81453C80FC53B453180C0A264CE021D38D5B4540B1BBB0C378B80F2DF7389" +
                    "027593C08DDEF9F47934B9CF805",
            new WorkSolution("7f5c2eb5e2658e81"),
            "1betagcfp7ojzzkof35peohaakx7mt9ddnj4ya8n7sa8imae4ttn",
            new NetworkConstants.WorkDifficultiesV2(
                    0xfffff00000000000L, 0xffffe00000000000L));
    
    /**
     * Constants representing the official Banano network.
     * @see <a href="https://banano.cc">Banano official website</a>
     */
    public static final NetworkConstants BANANO_LIVE_NET = new NetworkConstants(
            "Banano live network",
            "ban",
            "1111111111111111111111111111111111111111111111111111",
            "533DCAB343547B93C4128E779848DEA5877D3278CB5EA948BB3A9AA1AE0DB293DE6D9DA4F69E8D1DDFA385F9B4C5E4F38DFA4" +
                    "2C00D7B183560435D07AFA18900",
            new WorkSolution("fa055f79fa56abcf"),
            "1bananobh5rat99qfgt1ptpieie5swmoth87thi74qgbfrij7dcg",
            new NetworkConstants.WorkDifficultiesV1(0xfffffe0000000000L));
    
    
    /**
     * Returns a {@link NetworkConstants} instance for the specified network by matching the genesis block hash.
     * @param hash the hash of the genesis block
     * @return the associated {@link NetworkConstants} class, or null if not found
     */
    public static NetworkConstants getNetworkFromGenesisHash(String hash) {
        if (hash == null)
            throw new IllegalArgumentException("Block hash cannot be null.");
        if (!JNH.isValidHex(hash, 64))
            throw new IllegalArgumentException("Block hash is not a valid 64-character hex string.");
        
        hash = hash.toUpperCase();
        if (NANO_LIVE_NET.getNetworkIdentifier().equals(hash))
            return NANO_LIVE_NET;
        if (NANO_BETA_NET.getNetworkIdentifier().equals(hash))
            return NANO_BETA_NET;
        if (BANANO_LIVE_NET.getNetworkIdentifier().equals(hash))
            return BANANO_LIVE_NET;
        return null;
    }
    
    
    
    /**
     * The genesis account's address on the live Nano network.
     * @deprecated Use constants offered in {@link #NANO_LIVE_NET}.
     */
    @Deprecated(forRemoval = true)
    public static final NanoAccount ADDRESS_GENESIS_LIVE = NANO_LIVE_NET.getGenesisAccount();
    
    /**
     * The official designated burn address. Funds sent to this address will be permanently irretrievable.
     * @deprecated Use constants offered in {@link #NANO_LIVE_NET}.
     */
    @Deprecated(forRemoval = true)
    public static final NanoAccount ADDRESS_BURN = NANO_LIVE_NET.getBurnAddress();
    
    
    /**
     * The genesis block on the live Nano network. This represents the block/transaction where all Nano units were
     * initially introduced to the network.
     * @deprecated Use constants offered in {@link #NANO_LIVE_NET}.
     */
    @Deprecated(forRemoval = true)
    public static final Block BLOCK_GENESIS_LIVE = NANO_LIVE_NET.getGenesisBlock();
    
    /**
     * The block hash of the genesis transaction on the live Nano network. This represents the block/transaction where
     * all Nano units were initially introduced to the network.
     * @deprecated Use constants offered in {@link #NANO_LIVE_NET}.
     */
    @Deprecated(forRemoval = true)
    public static final String BLOCK_HASH_GENESIS = BLOCK_GENESIS_LIVE.getHash();
    
}
