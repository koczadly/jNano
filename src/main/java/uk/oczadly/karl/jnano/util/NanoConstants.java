/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util;

import uk.oczadly.karl.jnano.internal.JNC;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.internal.NanoConst;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.model.work.generator.policy.ConstantDifficultyPolicyV1;
import uk.oczadly.karl.jnano.model.work.generator.policy.ConstantDifficultyPolicyV2;

import java.math.BigInteger;
import java.util.Set;

/**
 * This class contains a set of static constant values. Any values which represent a state of the network are for
 * the official live Nano network only.
 */
public final class NanoConstants {
    private NanoConstants() {}
    
    
    /**
     * The maximum possible balance value, in raw units. This does not account for burned or undistributed funds, but
     * is simply the number of units which are generated within the genesis block.
     *
     * @deprecated Use of {@link uk.oczadly.karl.jnano.model.NanoAmount#MAX_VALUE} should be preferred.
     */
    @Deprecated
    public static final BigInteger MAX_BALANCE_RAW = JNC.BIGINT_MAX_128;
    
    /**
     * Constants representing the official live Nano network.
     * @see <a href="https://nano.org">Nano official website</a>
     */
    public static final NetworkConstants NANO_LIVE_NET = new NetworkConstants(
            new NetworkConstants.Version(21, 2),
            "Nano live", NanoAccount.DEFAULT_PREFIX,
            "1111111111111111111111111111111111111111111111111111",
            "9F0C933C8ADE004D808EA1985FA746A7E95BA2A38F867640F53EC8F180BDFE9E2C1268DEAD7C2664F356E37ABA362BC58E46D" +
                    "BA03E523A7B5A19E4B6EB12BB02",
            new WorkSolution("62f05417dd3fb691"), "3t6k35gi95xu6tergt6p69ck76ogmitsa8mnijtpxm9fkcm736xt",
            new ConstantDifficultyPolicyV2(
                    new WorkDifficulty(0xfffffff800000000L), new WorkDifficulty(0xfffffe0000000000L)));
    
    /**
     * Constants representing the official beta Nano network.
     * @see <a href="https://beta.nano.org">Nano beta website</a>
     */
    public static final NetworkConstants NANO_BETA_NET = new NetworkConstants(
            new NetworkConstants.Version(21, 2),
            "Nano beta", NanoAccount.DEFAULT_PREFIX,
            "1111111111111111111111111111111111111111111111111111",
            "DB9EFAC98A28EEA048E722F91C2A2720E1D8EF2A81453C80FC53B453180C0A264CE021D38D5B4540B1BBB0C378B80F2DF7389" +
                    "027593C08DDEF9F47934B9CF805",
            new WorkSolution("7f5c2eb5e2658e81"), "1betagcfp7ojzzkof35peohaakx7mt9ddnj4ya8n7sa8imae4ttn",
            new ConstantDifficultyPolicyV2(
                    new WorkDifficulty(0xfffff00000000000L), new WorkDifficulty(0xffffe00000000000L)));
    
    /**
     * Constants representing the official Banano network.
     * @see <a href="https://banano.cc">Banano official website</a>
     */
    public static final NetworkConstants BANANO_LIVE_NET = new NetworkConstants(
            new NetworkConstants.Version(20, 0),
            "Banano live", "ban",
            "1111111111111111111111111111111111111111111111111111",
            "533DCAB343547B93C4128E779848DEA5877D3278CB5EA948BB3A9AA1AE0DB293DE6D9DA4F69E8D1DDFA385F9B4C5E4F38DFA4" +
                    "2C00D7B183560435D07AFA18900",
            new WorkSolution("fa055f79fa56abcf"), "1bananobh5rat99qfgt1ptpieie5swmoth87thi74qgbfrij7dcg",
            new ConstantDifficultyPolicyV1(new WorkDifficulty(0xfffffe0000000000L)));
    
    
    /** An immutable set of <em>all</em> available network constant instances.
     * <p>Currently contains: {@link #NANO_LIVE_NET}, {@link #NANO_BETA_NET} and {@link #BANANO_LIVE_NET}.</p>*/
    public static final Set<NetworkConstants> ALL_NETWORKS = JNH.ofSet(NANO_LIVE_NET, NANO_BETA_NET, BANANO_LIVE_NET);
    
    
    /**
     * Returns a {@link NetworkConstants} instance for the specified network by matching the genesis block hash.
     * @param hash the hash of the genesis block
     * @return the associated {@link NetworkConstants} class, or null if not found
     */
    public static NetworkConstants getNetworkFromGenesisHash(String hash) {
        if (hash == null)
            throw new IllegalArgumentException("Block hash cannot be null.");
        if (!JNH.isValidHex(hash, NanoConst.LEN_HASH_H))
            throw new IllegalArgumentException("Block hash is not a valid 64-character hex string.");
        
        return ALL_NETWORKS.stream()
                .filter(net -> net.getNetworkIdentifier().equalsIgnoreCase(hash))
                .findFirst().orElse(null);
    }
    
}
