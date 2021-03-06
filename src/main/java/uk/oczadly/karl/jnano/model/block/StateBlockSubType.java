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
    
    SEND    (true,  true,  true,  BlockType.SEND,    LinkData.Intent.DESTINATION_ACCOUNT),
    RECEIVE (true,  true,  true,  BlockType.RECEIVE, LinkData.Intent.SOURCE_HASH),
    OPEN    (true,  false, true,  BlockType.OPEN,    LinkData.Intent.SOURCE_HASH),
    CHANGE  (false, true,  true,  BlockType.CHANGE,  LinkData.Intent.UNUSED),
    EPOCH   (false, false, false, null,              LinkData.Intent.EPOCH_IDENTIFIER);
    
    
    static final Map<BlockType, StateBlockSubType> LEGACY_LOOKUP_MAP = new HashMap<>();
    
    static { // Initialize lookup maps
        for (StateBlockSubType type : values()) {
            if (type.getLegacyType() != null)
                LEGACY_LOOKUP_MAP.put(type.getLegacyType(), type);
        }
    }
    
    
    private final boolean isTransaction, requiresPrevious, isSelfSigned;
    private final BlockType legacyType;
    private final LinkData.Intent linkIntent;
    
    StateBlockSubType(boolean isTransaction, boolean requiresPrevious, boolean isSelfSigned, BlockType legacyType,
                      LinkData.Intent linkIntent) {
        this.isTransaction = isTransaction;
        this.requiresPrevious = requiresPrevious;
        this.isSelfSigned = isSelfSigned;
        this.legacyType = legacyType;
        this.linkIntent = linkIntent;
    }
    
    
    /**
     * @return whether the block subtype represents a transfer of funds
     */
    public boolean isTransaction() {
        return isTransaction;
    }
    
    /**
     * @return true if this block type requires the account to already be opened and the {@code previous} field to be
     *         set, false if the type may be an opening block for the account
     */
    public boolean requiresPrevious() {
        return requiresPrevious;
    }
    
    /**
     * @return true if this subtype must be signed by the account holder, false if it <em>may</em> be signed by a
     *         different account under certain circumstances (eg. epoch blocks)
     */
    public boolean isSelfSigned() {
        return isSelfSigned;
    }
    
    /**
     * @return the official protocol name of this subtype, used in the {@code subtype} field
     */
    public String getProtocolName() {
        return name().toLowerCase();
    }
    
    /**
     * @return the legacy {@link BlockType} which represents the same action, or null if there is no corresponding type
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
