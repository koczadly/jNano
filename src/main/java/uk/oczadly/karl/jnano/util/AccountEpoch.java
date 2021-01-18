/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.StateBlock;
import uk.oczadly.karl.jnano.model.block.StateBlockSubType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This enum class represents account version epochs for the live Nano network.
 */
public enum AccountEpoch {
    
    /**
     * Version 1 epoch block, which marks the transition from legacy blocks (send/receive/open/change) to UTX state
     * blocks.
     */
    V1 ("65706F636820763120626C6F636B000000000000000000000000000000000000",
            NanoConstants.NANO_LIVE_NET.getGenesisAccount()),
    
    /**
     * Version 2 epoch block, which marks the change in minimum work difficulties introduced in node V21.
     */
    V2 ("65706F636820763220626C6F636B000000000000000000000000000000000000",
            NanoAccount.parseAddressSegment("3qb6o6i1tkzr6jwr5s7eehfxwg9x6eemitdinbpi7u8bjjwsgqfj"));
    
    
    public static final AccountEpoch LATEST_EPOCH = values()[values().length - 1];
    
    private static final Map<Integer, AccountEpoch> VER_MAP = new HashMap<>();
    private static final Map<String, AccountEpoch> ID_MAP = new HashMap<>();
    
    static {
        for (AccountEpoch e : AccountEpoch.values()) {
            VER_MAP.put(e.getVersion(), e);
            ID_MAP.put(e.getIdentifier().toHexString(), e);
        }
    }
    
    HexData id;
    NanoAccount signerAcc;
    
    AccountEpoch(String id, NanoAccount signerAcc) {
        this.id = new HexData(id);
        this.signerAcc = signerAcc;
    }
    
    
    /**
     * @return the version which this epoch block will upgrade an account to
     */
    public int getVersion() {
        return ordinal() + 1;
    }
    
    /**
     * @return the identifier (block {@code link} data) of this epoch upgrade
     */
    public HexData getIdentifier() {
        return id;
    }
    
    /**
     * @return the account used to sign the epoch block, or null if the owner's account is used instead
     */
    public NanoAccount getSignerAccount() {
        return signerAcc;
    }
    
    
    /**
     * Returns the appropriate epoch transition from a given version integer.
     * @param ver the version which the epoch block will upgrade the account to
     * @return the related epoch transition, or null if not found
     */
    public static AccountEpoch fromVersion(int ver) {
        return VER_MAP.get(ver);
    }
    
    /**
     * Returns the appropriate epoch transition from a given identifier.
     * @param id the identifier of the epoch transition (link data value)
     * @return the related epoch transition, or null if not found
     */
    public static AccountEpoch fromIdentifier(HexData id) {
        return fromIdentifier(id.toHexString());
    }
    
    /**
     * Returns the appropriate epoch transition from a given identifier.
     * @param id the identifier of the epoch transition (link data value)
     * @return the related epoch transition, or null if not found
     */
    public static AccountEpoch fromIdentifier(String id) {
        return ID_MAP.get(id.toUpperCase());
    }
    
    /**
     * Retrieves the corresponding {@link AccountEpoch} from a given epoch block. In cases where the given block is
     * not an epoch block, this method will return null.
     * @param block the epoch block
     * @return the epoch version which this block represents, or null if the block isn't an epoch block
     */
    public static AccountEpoch fromEpochBlock(Block block) {
        if (block instanceof StateBlock) {
            StateBlock sb = (StateBlock)block;
            if (sb.getSubType() == null || sb.getSubType() == StateBlockSubType.EPOCH) {
                AccountEpoch epoch = fromIdentifier(sb.getLinkData());
                if (epoch == null)
                    throw new IllegalArgumentException("Unknown epoch identifier " + sb.getLinkData() + ".");
                return epoch;
            }
        }
        return null;
    }
    
    /**
     * Calculates the account version from a given set of blocks. This method will ignore all non-epoch blocks, and
     * return the latest upgrade found in the list. In cases where no epoch blocks are present, this method will
     * return null.
     * @param blocks a set of blocks within a certain account
     * @return the latest epoch block in the list, or null if no epoch blocks are declared
     */
    public static AccountEpoch calculateAccountVersion(Collection<Block> blocks) {
        if (blocks == null)
            throw new IllegalArgumentException("Blocks collection cannot be null.");
        
        AccountEpoch latestEpoch = null;
        for (Block b : blocks) {
            AccountEpoch epoch = fromEpochBlock(b);
            if (epoch != null && (latestEpoch == null || epoch.compareTo(latestEpoch) > 0))
                latestEpoch = epoch;
        }
        return latestEpoch;
    }
    
}
