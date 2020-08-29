/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains enum instances which each represent a sub-type of state block.
 */
public enum StateBlockSubType {
    
    @SerializedName("send")
    SEND        (true,   BlockType.SEND),
    
    @SerializedName("receive")
    RECEIVE     (true,   BlockType.RECEIVE),
    
    @SerializedName("open")
    OPEN        (true,   BlockType.OPEN),
    
    @SerializedName("change")
    CHANGE      (false,  BlockType.CHANGE),
    
    @SerializedName("epoch")
    EPOCH       (false,  null);
    
    
    /**
     * @deprecated Use {@link #CHANGE} instead
     */
    @Deprecated(forRemoval = true)
    public static final StateBlockSubType CHANGE_REP = CHANGE;
    
    
    static final Map<BlockType, StateBlockSubType> LEGACY_LOOKUP_MAP = new HashMap<>();
    
    static { // Initialize lookup maps
        for (StateBlockSubType type : values()) {
            if (type.getLegacyType() != null)
                LEGACY_LOOKUP_MAP.put(type.getLegacyType(), type);
        }
    }
    
    
    boolean isTransaction;
    BlockType legacyType;
    
    StateBlockSubType(boolean isTransaction, BlockType legacyType) {
        this.isTransaction = isTransaction;
        this.legacyType = legacyType;
    }
    
    
    /**
     * @return whether the block (sub)type represents a transfer of funds
     */
    public boolean isTransaction() {
        return isTransaction;
    }
    
    /**
     * @return the official protocol name of this subtype
     */
    public String getProtocolName() {
        return name().toLowerCase();
    }
    
    /**
     * @return the legacy {@link BlockType} which represents the same activity, or null if there isn't one
     */
    public BlockType getLegacyType() {
        return legacyType;
    }
    
    @Override
    public String toString() {
        return getProtocolName();
    }
    
    
    /**
     * Returns the enum constant of this type corresponding to the legacy {@code BlockType}.
     *
     * @param type the legacy block type
     * @return the corresponding subtype, or null if incompatible
     */
    public static StateBlockSubType getFromLegacyType(BlockType type) {
        return LEGACY_LOOKUP_MAP.get(type);
    }
    
    /**
     * Returns the enum constant of this type from the given official name.
     *
     * @param name the official type name
     * @return the corresponding subtype, or null if not found
     */
    public static StateBlockSubType getFromName(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
}
