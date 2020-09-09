/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util;

import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.OpenBlock;
import uk.oczadly.karl.jnano.model.block.StateBlock;
import uk.oczadly.karl.jnano.model.block.StateBlockSubType;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

public final class NetworkConstants {
    
    private final String networkName, addressPrefix;
    private final OpenBlock genesisBlock;
    private final NanoAccount burnAddress;
    private final WorkDifficulties workDifficulty;
    
    NetworkConstants(String networkName, String addressPrefix, String burnAddressSegment, String genBlockSig,
                     WorkSolution genBlockWork, String genBlockAccountSeg, WorkDifficulties workDifficulty) {
        this.networkName = networkName + " network";
        this.addressPrefix = addressPrefix;
        this.burnAddress = NanoAccount.parseAddressSegment(burnAddressSegment, addressPrefix);
        NanoAccount genesisAccount = NanoAccount.parseAddressSegment(genBlockAccountSeg, addressPrefix);
        this.genesisBlock = new OpenBlock(genBlockSig, genBlockWork, genesisAccount.toPublicKey(), genesisAccount,
                genesisAccount);
        this.workDifficulty = workDifficulty;
    }
    
    
    /**
     * @return the name of this network
     */
    public String getNetworkName() {
        return networkName;
    }
    
    /**
     * @return the genesis block which created all existing nano units
     */
    public OpenBlock getGenesisBlock() {
        return genesisBlock;
    }
    
    /**
     * @return the account which created the genesis block
     */
    public NanoAccount getGenesisAccount() {
        return getGenesisBlock().getAccount();
    }
    
    /**
     * @return the network identifier string (genesis block hash)
     */
    public String getNetworkIdentifier() {
        return getGenesisBlock().getHash();
    }
    
    /**
     * @return the officially designated burn address, where coins are made irretrievable
     */
    public NanoAccount getBurnAddress() {
        return burnAddress;
    }
    
    /**
     * @return the prefix of account addresses
     */
    public String getAddressPrefix() {
        return addressPrefix;
    }
    
    /**
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
            return getBase();
        }
    
        /**
         * Returns the minimum work difficulty threshold for a given block. Be aware that this method does
         * <em>not</em> factor in different difficulty epochs, and is only suitable for newly produced blocks.
         * @param blockType the block type to compute work for
         * @return the work difficulty threshold for the given block type
         */
        default WorkDifficulty getForType(StateBlockSubType blockType) {
            if (blockType == null) throw new IllegalArgumentException("Block type cannot be null.");
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
        private final WorkDifficulty send, receive, base;
        
        WorkDifficultiesV2(long send, long receive) {
            this.send = new WorkDifficulty(send);
            this.receive = new WorkDifficulty(receive);
            this.base = JNH.max(this.send, this.receive);
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
        public WorkDifficulty getBase() {
            return base;
        }
    }
    
}
