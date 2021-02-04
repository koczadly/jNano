/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.epoch;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.StateBlock;
import uk.oczadly.karl.jnano.model.block.StateBlockSubType;

import java.util.*;

/**
 * This class contains and represents a set of supported account epoch upgrades for a given network.
 */
public class EpochUpgradeRegistry {
    
    private final Map<Integer, EpochUpgrade> mapByVer = new HashMap<>();
    private final Map<HexData, EpochUpgrade> mapById = new HashMap<>();
    private final SortedSet<EpochUpgrade> upgrades;
    
    /**
     * @param upgrades the supported upgrades
     */
    public EpochUpgradeRegistry(EpochUpgrade... upgrades) {
        if (upgrades == null)
            throw new IllegalArgumentException("Versions array cannot be null.");
        
        SortedSet<EpochUpgrade> allUpgrades = new TreeSet<>();
        for (EpochUpgrade ver : upgrades) {
            if (ver == null)
                throw new IllegalArgumentException("EpochVersion array elements cannot be null.");
            
            allUpgrades.add(ver);
            boolean verExists = mapByVer.put(ver.getVersion(), ver) != null;
            boolean idExists = mapById.put(ver.getIdentifier(), ver) != null;
            
            if (verExists || idExists)
                throw new IllegalArgumentException("Overlapping version or identifier values.");
        }
        
        this.upgrades = Collections.unmodifiableSortedSet(allUpgrades);
    }
    
    
    /**
     * Returns an ordered set of supported epoch upgrades.
     * @return the supported epoch upgrades
     */
    public SortedSet<EpochUpgrade> getUpgrades() {
        return upgrades;
    }
    
    /**
     * Returns the {@link EpochUpgrade} which corresponds to the given version.
     * @param version the version
     * @return the corresponding upgrade
     * @throws UnrecognizedEpochException if no matching upgrade is found
     */
    public EpochUpgrade fromVersion(int version) {
        EpochUpgrade result = mapByVer.get(version);
        if (result == null)
            throw new UnrecognizedEpochException("Could not find epoch by version integer.");
        return result;
    }
    
    /**
     * Returns the {@link EpochUpgrade} which corresponds to the given identifier.
     * @param identifier the identifier ({@code link} data)
     * @return the corresponding upgrade
     * @throws UnrecognizedEpochException if no matching upgrade is found
     */
    public EpochUpgrade fromIdentifier(HexData identifier) {
        EpochUpgrade result = mapById.get(identifier);
        if (result == null)
            throw new UnrecognizedEpochException("Could not find epoch by identifier.");
        return result;
    }
    
    /**
     * Returns the {@link EpochUpgrade} which the given epoch block represents, or null if the supplied block is not an
     * epoch block.
     * @param block the block
     * @return the corresponding upgrade, or null if the block is not an epoch block
     * @throws UnrecognizedEpochException if the block is an epoch block, but no matching upgrade is found
     */
    public EpochUpgrade fromEpochBlock(Block block) {
        if (block instanceof StateBlock) {
            StateBlock sb = (StateBlock)block;
            if (sb.getSubType() == StateBlockSubType.EPOCH)
                return fromIdentifier(sb.getLink().asHex());
        }
        return null;
    }
    
}
