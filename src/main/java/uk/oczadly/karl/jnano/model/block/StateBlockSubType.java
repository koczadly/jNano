/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * This class contains enum instances which each represent a sub-type of state block.
 */
@JsonAdapter(StateBlockSubType.JsonAdapter.class)
public enum StateBlockSubType {
    
    SEND        (true,   BlockType.SEND),
    RECEIVE     (true,   BlockType.RECEIVE),
    OPEN        (true,   BlockType.OPEN),
    CHANGE      (false,  BlockType.CHANGE),
    EPOCH       (false,  null);
    
    
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
    
    
    static class JsonAdapter implements JsonDeserializer<StateBlockSubType>, JsonSerializer<StateBlockSubType> {
        @Override
        public JsonElement serialize(StateBlockSubType src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getProtocolName());
        }
        @Override
        public StateBlockSubType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            StateBlockSubType subtype = StateBlockSubType.getFromName(json.getAsString());
            if (subtype == null) throw new JsonParseException("Unrecognized StateBlockSubType value.");
            return subtype;
        }
    }
    
}
