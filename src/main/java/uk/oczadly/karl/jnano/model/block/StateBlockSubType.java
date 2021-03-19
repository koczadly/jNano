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
    
    SEND    (true,  true,  BlockType.SEND,    LinkData.Intent.DESTINATION_ACCOUNT),
    RECEIVE (true,  true,  BlockType.RECEIVE, LinkData.Intent.SOURCE_HASH),
    OPEN    (true,  false, BlockType.OPEN,    LinkData.Intent.SOURCE_HASH),
    CHANGE  (false, true,  BlockType.CHANGE,  LinkData.Intent.UNUSED),
    EPOCH   (false, false, null,              LinkData.Intent.EPOCH_IDENTIFIER);
    
    
    static final Map<BlockType, StateBlockSubType> LEGACY_LOOKUP_MAP = new HashMap<>();
    
    static { // Initialize lookup maps
        for (StateBlockSubType type : values()) {
            if (type.getLegacyType() != null)
                LEGACY_LOOKUP_MAP.put(type.getLegacyType(), type);
        }
    }
    
    
    private final boolean isTransaction, requiresPrevious;
    private final BlockType legacyType;
    private final LinkData.Intent linkIntent;
    
    StateBlockSubType(boolean isTransaction, boolean requiresPrevious, BlockType legacyType,
                      LinkData.Intent linkIntent) {
        this.isTransaction = isTransaction;
        this.requiresPrevious = requiresPrevious;
        this.legacyType = legacyType;
        this.linkIntent = linkIntent;
    }
    
    
    /**
     * @return whether the block (sub)type represents a transfer of funds
     */
    public boolean isTransaction() {
        return isTransaction;
    }
    
    boolean requiresPrevious() {
        return requiresPrevious;
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
    
    /**
     * @return the intent of the {@code link} field for this transaction type
     */
    public LinkData.Intent getLinkIntent() {
        return linkIntent;
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
