/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.OpenBlock;
import uk.oczadly.karl.jnano.model.epoch.EpochUpgradeRegistry;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.util.workgen.policy.ConstantWorkDifficultyPolicy;

/**
 * This class contains and represents a collection of constant values for a specific network or fork of the Nano
 * cryptocurrency.
 *
 * <p>Note that these constants may change as the node upgrades. In these cases, the jNano library will also need to
 * be updated to reflect these changes, otherwise this information may be outdated and no longer valid.</p>
 */
public final class NetworkConstants {
    
    private final String networkName, addressPrefix;
    private final OpenBlock genesisBlock;
    private final ConstantWorkDifficultyPolicy workDifficulty;
    private final EpochUpgradeRegistry epochs;
    
    NetworkConstants(String networkName, String addressPrefix, String genBlockSig,
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
     * Returns the genesis block which created all existing nano units.
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
        return getNetworkName() + " network";
    }
    
    
    private static OpenBlock createGenesisBlock(NanoAccount account, WorkSolution work, HexData signature) {
        return new OpenBlock(signature, work, new HexData(account.toPublicKey()), account, account);
    }
    
}
