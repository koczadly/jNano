/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.OpenBlock;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.model.work.generator.policy.ConstantWorkDifficultyPolicy;

import java.util.Comparator;
import java.util.Objects;

/**
 * This class contains and represents a collection of constant values for a specific network or fork of the Nano
 * cryptocurrency.
 *
 * <p>Note that these values may not match if the node version doesn't correlate with the value returned by
 * {@link #getIntendedVersion()}. These values are also not guaranteed to match in cases where canary blocks or
 * epochs trigger a change to the network policies (as seen with adjustments to work generation).</p>
 */
public final class NetworkConstants {
    
    private final Version version;
    private final String networkName, addressPrefix;
    private final OpenBlock genesisBlock;
    private final NanoAccount burnAddress;
    private final ConstantWorkDifficultyPolicy workDifficulty;
    
    NetworkConstants(Version version, String networkName, String addressPrefix, String burnAddressSegment,
                     String genBlockSig, WorkSolution genBlockWork, String genBlockAccountSeg,
                     ConstantWorkDifficultyPolicy workDifficulty) {
        this.version = version;
        this.networkName = networkName + " network";
        this.addressPrefix = addressPrefix;
        this.burnAddress = NanoAccount.parseAddressSegment(burnAddressSegment, addressPrefix);
        NanoAccount genesisAccount = NanoAccount.parseAddressSegment(genBlockAccountSeg, addressPrefix);
        this.genesisBlock = new OpenBlock(new HexData(genBlockSig), genBlockWork,
                new HexData(genesisAccount.toPublicKey()), genesisAccount, genesisAccount);
        this.workDifficulty = workDifficulty;
    }
    
    
    /**
     * Returns the node version which this set of constants is intended for. If using a non-matching version, some of
     * these constants may be invalid.
     * @return the intended node version
     */
    public Version getIntendedVersion() {
        return version;
    }
    
    /**
     * Returns the name of the network.
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
        return burnAddress;
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
    
    @Override
    public String toString() {
        return getNetworkName();
    }
    
    
    /** Represents a version of the node. Contains both a major and minor element. */
    public static final class Version implements Comparable<Version> {
        private final int major, minor;
        
        Version(int major, int minor) {
            if (major < 1)
                throw new IllegalArgumentException("Major version must be 1 or greater.");
            if (minor < 0)
                throw new IllegalArgumentException("Minor version must be 0 or greater.");
            this.major = major;
            this.minor = minor;
        }
        
        
        public int getMajor() {
            return major;
        }
        
        public int getMinor() {
            return minor;
        }
    
        @Override
        public String toString() {
            return "V" + major + "." + minor;
        }
        
        @Override
        public int compareTo(Version o) {
            if (o == null)
                throw new NullPointerException("Argument cannot be null.");
            return Comparator.comparingInt(Version::getMajor).thenComparing(Version::getMinor).compare(this, o);
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Version)) return false;
            Version version = (Version)o;
            return getMajor() == version.getMajor() &&
                    getMinor() == version.getMinor();
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(getMajor(), getMinor());
        }
    }
    
}
