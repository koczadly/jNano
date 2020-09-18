/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util;

import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.*;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

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
    private final WorkDifficulties workDifficulty;
    
    NetworkConstants(Version version, String networkName, String addressPrefix, String burnAddressSegment,
                     String genBlockSig, WorkSolution genBlockWork, String genBlockAccountSeg,
                     WorkDifficulties workDifficulty) {
        this.version = version;
        this.networkName = networkName + " network";
        this.addressPrefix = addressPrefix;
        this.burnAddress = NanoAccount.parseAddressSegment(burnAddressSegment, addressPrefix);
        NanoAccount genesisAccount = NanoAccount.parseAddressSegment(genBlockAccountSeg, addressPrefix);
        this.genesisBlock = new OpenBlock(genBlockSig, genBlockWork, genesisAccount.toPublicKey(), genesisAccount,
                genesisAccount);
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
        return getGenesisBlock().getHash();
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
     * Returns a {@link WorkDifficulties} object which contains the minimum work difficulty policy for this network.
     * @return the work difficulty thresholds for this network
     */
    public WorkDifficulties getWorkDifficulties() {
        return workDifficulty;
    }
    
    /**
     * @param blockType the block type to compute work for
     * @return the work difficulty threshold for the given block type
     * @deprecated Use of {@link #getWorkDifficulties()} is recommended
     */
    @Deprecated
    public WorkDifficulty getWorkDifficultyThreshold(StateBlockSubType blockType) {
        return getWorkDifficulties().getForType(blockType);
    }
    
    /**
     * @return the base work difficulty threshold for any block type
     * @deprecated Use of {@link #getWorkDifficulties()} is recommended
     */
    @Deprecated
    public WorkDifficulty getWorkDifficultyThreshold() {
        return getWorkDifficulties().getBase();
    }
    
    @Override
    public String toString() {
        return getNetworkName();
    }
    
    
    /** Represents a set of minimum work difficulty policies for the network. */
    public interface WorkDifficulties {
        /**
         * Returns the minimum work difficulty threshold for a given block. Be aware that this method does
         * <em>not</em> factor in different difficulty epochs, and is only suitable for newly produced blocks.
         * @param block the block to compute work for
         * @return the work difficulty threshold for the given block
         */
        default WorkDifficulty getForBlock(Block block) {
            if (block == null) throw new IllegalArgumentException("Block cannot be null.");
            if (block instanceof StateBlock) {
                StateBlockSubType subtype = ((StateBlock)block).getSubType();
                if (subtype != null) {
                    return getForType(subtype);
                }
            }
            return block.getType() != null ? getForType(block.getType()) : getBase();
        }
    
        /**
         * Returns the minimum work difficulty threshold for a given state block type. Be aware that this method does
         * <em>not</em> factor in different difficulty epochs, and is only suitable for newly produced blocks.
         * @param type the block type to compute work for
         * @return the work difficulty threshold for the given block type
         */
        default WorkDifficulty getForType(StateBlockSubType type) {
            return getForType(BlockType.STATE);
        }
    
        /**
         * Returns the minimum work difficulty threshold for a given block type. Be aware that this method does
         * <em>not</em> factor in different difficulty epochs, and is only suitable for newly produced blocks.
         * @param type the block type to compute work for
         * @return the work difficulty threshold for the given block type
         */
        default WorkDifficulty getForType(BlockType type) {
            return getBase();
        }
    
        /**
         * Returns the minimum work difficulty threshold for a given block. Be aware that this method does
         * <em>not</em> factor in different difficulty epochs, and is only suitable for newly produced blocks.
         * @return the base work difficulty threshold for any block type
         */
        WorkDifficulty getBase();
    }
    
    static class WorkDifficultiesV1 implements WorkDifficulties {
        private final WorkDifficulty diff;
        
        WorkDifficultiesV1(long diff) {
            this.diff = new WorkDifficulty(diff);
        }
        
        @Override
        public WorkDifficulty getBase() {
            return diff;
        }
    }
    
    static class WorkDifficultiesV2 implements WorkDifficulties {
        private final WorkDifficulty send, receive, base, legacy;
        
        WorkDifficultiesV2(long send, long receive, long legacy) {
            this.send = new WorkDifficulty(send);
            this.receive = new WorkDifficulty(receive);
            this.legacy = new WorkDifficulty(legacy);
            this.base = JNH.max(this.send, this.receive);
        }
    
        @Override
        public WorkDifficulty getForBlock(Block block) {
            if (block == null) throw new IllegalArgumentException("Block cannot be null.");
            if (block instanceof StateBlock) {
                StateBlockSubType subtype = ((StateBlock)block).getSubType();
                if (subtype != null) {
                    return getForType(subtype);
                }
            } else {
                return legacy;
            }
            return block.getType() != null ? getForType(block.getType()) : getBase();
        }
        
        @Override
        public WorkDifficulty getForType(StateBlockSubType subtype) {
            switch (subtype) {
                case SEND:
                case CHANGE:
                    return send;
                default:
                    return receive;
            }
        }
    
        @Override
        public WorkDifficulty getForType(BlockType type) {
            switch (type) {
                case SEND:
                case RECEIVE:
                case OPEN:
                case CHANGE:
                    return legacy;
                default:
                    return base;
            }
        }
    
        @Override
        public WorkDifficulty getBase() {
            return base;
        }
    }
    
    /** Represents a version of the node. Contains both a major and minor element. */
    public static final class Version implements Comparable<Version> {
        private final int major, minor;
        
        public Version(int major, int minor) {
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
