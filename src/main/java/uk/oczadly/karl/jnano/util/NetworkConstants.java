/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util;

import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.internal.NanoConst;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.OpenBlock;
import uk.oczadly.karl.jnano.model.epoch.EpochUpgrade;
import uk.oczadly.karl.jnano.model.epoch.EpochUpgradeRegistry;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.util.workgen.policy.ConstantDifficultyPolicy;
import uk.oczadly.karl.jnano.util.workgen.policy.ConstantDifficultyPolicyV2;
import uk.oczadly.karl.jnano.util.workgen.policy.ConstantWorkDifficultyPolicy;

import java.util.Set;

/**
 * This class contains a set of static constant values for the Nano network, and other forks.
 */
public final class NetworkConstants {
    
    /**
     * Constants for the official live Nano network.
     * @see <a href="https://nano.org">Nano official website</a>
     */
    public static final NetworkConstants NANO = new NetworkConstants(
            "Nano", NanoAccount.DEFAULT_PREFIX,
            "9F0C933C8ADE004D808EA1985FA746A7E95BA2A38F867640F53EC8F180BDFE9E2C1268DEAD7C2664F356E37ABA362BC58E46D" +
                    "BA03E523A7B5A19E4B6EB12BB02", new WorkSolution("62f05417dd3fb691"),
            "E89208DD038FBB269987689621D52292AE9C35941A7484756ECCED92A65093BA",
            new ConstantDifficultyPolicyV2(
                    new WorkDifficulty("fffffff800000000"), new WorkDifficulty("fffffe0000000000")),
            new EpochUpgradeRegistry(
                    new EpochUpgrade(1, NanoAccount.parsePublicKey(
                            "E89208DD038FBB269987689621D52292AE9C35941A7484756ECCED92A65093BA")),
                    new EpochUpgrade(2, NanoAccount.parsePublicKey(
                            "DD24A9200D4BF8247981E4AC63DBDE38FD2319386970A26D02ECC98C79975DB1"))
            ));
    
    /**
     * Constants for the official Nano Beta network.
     * @see <a href="https://beta.nano.org">Nano beta website</a>
     */
    public static final NetworkConstants NANO_BETA = new NetworkConstants(
            "Nano (Beta)", NanoAccount.DEFAULT_PREFIX,
            "2F4D72B8E973C979E4D6815CB34C2F426AD997FB8BC6BD94C92541E7F35879594A392AA0B28D0A865EA4C73DB2DE56893E947FD" +
                    "0AD76AB847A2BB5AEDFBF0E00", new WorkSolution("a870b0e9331cf477"),
            "A59A439B34662385D48F7FF9CA50030F889BAA9AC320EA5A85AAD777CF82B088",
            new ConstantDifficultyPolicyV2(
                    new WorkDifficulty("fffff00000000000"), new WorkDifficulty("ffffe00000000000")),
            new EpochUpgradeRegistry(
                    new EpochUpgrade(1, NanoAccount.parsePublicKey(
                            "A59A439B34662385D48F7FF9CA50030F889BAA9AC320EA5A85AAD777CF82B088")),
                    new EpochUpgrade(2, NanoAccount.parsePublicKey(
                            "A59A439B34662385D48F7FF9CA50030F889BAA9AC320EA5A85AAD777CF82B088"))
            ));
    
    /**
     * Constants for the official Banano network.
     * @see <a href="https://banano.cc">Banano official website</a>
     */
    public static final NetworkConstants BANANO = new NetworkConstants(
            "Banano", "ban",
            "533DCAB343547B93C4128E779848DEA5877D3278CB5EA948BB3A9AA1AE0DB293DE6D9DA4F69E8D1DDFA385F9B4C5E4F38DFA4" +
                    "2C00D7B183560435D07AFA18900", new WorkSolution("fa055f79fa56abcf"),
            "2514452A978F08D1CF76BB40B6AD064183CF275D3CC5D3E0515DC96E2112AD4E",
            new ConstantDifficultyPolicy(new WorkDifficulty("fffffe0000000000")),
            new EpochUpgradeRegistry());
    
    
    /** An immutable set of all available network constant instances provided by this library.
     * <p>Currently contains: {@link #NANO}, {@link #NANO_BETA} and {@link #BANANO}.</p>*/
    public static final Set<NetworkConstants> SUPPORTED_NETWORKS = JNH.ofSet(NANO, NANO_BETA, BANANO);
    
    
    /**
     * Returns a {@link NetworkConstants} instance for the specified network by matching the genesis block hash.
     * @param hash the hash of the genesis block
     * @return the associated {@link NetworkConstants} class, or null if not found
     */
    public static NetworkConstants fromIdentifier(String hash) {
        if (hash == null)
            throw new IllegalArgumentException("Block hash cannot be null.");
        if (!JNH.isValidHex(hash, NanoConst.LEN_HASH_H))
            throw new IllegalArgumentException("Block hash is not a valid 64-character hex string.");
        
        return SUPPORTED_NETWORKS.stream()
                .filter(net -> net.getNetworkIdentifier().equalsIgnoreCase(hash))
                .findFirst().orElse(null);
    }
    
    
    private final String networkName, addressPrefix;
    private final OpenBlock genesisBlock;
    private final ConstantWorkDifficultyPolicy workDifficulty;
    private final EpochUpgradeRegistry epochs;
    
    private NetworkConstants(String networkName, String addressPrefix, String genBlockSig,
                             WorkSolution genBlockWork, String genAccountPk,
                             ConstantWorkDifficultyPolicy workDifficulty, EpochUpgradeRegistry epochs) {
        this.networkName = networkName;
        this.addressPrefix = addressPrefix;
        NanoAccount genesisAccount = NanoAccount.parsePublicKey(genAccountPk, addressPrefix);
        this.genesisBlock = createGenesisBlock(genesisAccount, genBlockWork, new HexData(genBlockSig));
        this.workDifficulty = workDifficulty;
        this.epochs = epochs;
    }
    
    
    /**
     * Returns the name of this network.
     * @return the name of this network
     */
    public String getNetworkName() {
        return networkName;
    }
    
    /**
     * Returns the genesis block which created all existing transactional units.
     *
     * <p>Note that the returned object is immutable, and calling the setter methods will throw a
     * {@link UnsupportedOperationException} which may be unexpected behaviour.</p>
     *
     * @return the genesis block
     */
    public OpenBlock getGenesisBlock() {
        return genesisBlock;
    }
    
    /**
     * Returns the account which holds the genesis block.
     * @return the genesis account
     */
    public NanoAccount getGenesisAccount() {
        return getGenesisBlock().getAccount();
    }
    
    /**
     * Returns the network identifier string. This is also the genesis block hash.
     * @return the network identifier
     */
    public String getNetworkIdentifier() {
        return getGenesisBlock().getHash().toHexString();
    }
    
    /**
     * Returns the officially designated burn address, where coins are made irretrievable
     * @return the burn account
     */
    public NanoAccount getBurnAddress() {
        return NanoAccount.ZERO_ACCOUNT.withPrefix(getAddressPrefix());
    }
    
    /**
     * Returns the default address prefix used for accounts, which comes before the underscore symbol.
     * @return the prefix of account addresses
     */
    public String getAddressPrefix() {
        return addressPrefix;
    }
    
    /**
     * Returns the current minimum work difficulty thresholds for this network.
     * @return the minimum work difficulty thresholds
     */
    public ConstantWorkDifficultyPolicy getWorkDifficulties() {
        return workDifficulty;
    }
    
    /**
     * Returns a registry of supported and recognized account epoch upgrades.
     * @return a registry of epoch upgrades
     */
    public EpochUpgradeRegistry getEpochUpgrades() {
        return epochs;
    }
    
    @Override
    public String toString() {
        return "NetworkConstantValues{" +
                "name='" + networkName + "', " +
                "identifier='" + getNetworkIdentifier() + "'}";
    }
    
    private static OpenBlock createGenesisBlock(NanoAccount account, WorkSolution work, HexData signature) {
        OpenBlock block = new ImmutableOpenBlock(
                signature, work, new HexData(account.toPublicKey()), account, account);
        if (!block.verifySignature())
            throw new IllegalArgumentException("Signature is invalid.");
        return block;
    }
    
    private static class ImmutableOpenBlock extends OpenBlock {
        public ImmutableOpenBlock(HexData signature, WorkSolution work, HexData source, NanoAccount account,
                                  NanoAccount representative) {
            super(signature, work, source, account, representative);
        }
        
        @Override
        public synchronized void setSignature(HexData signature) {
            throw new UnsupportedOperationException("Block is immutable, so the signature cannot be changed.");
        }
        
        @Override
        public void setWorkSolution(WorkSolution work) {
            throw new UnsupportedOperationException("Block is immutable, so the work cannot be changed.");
        }
    }
    
}
