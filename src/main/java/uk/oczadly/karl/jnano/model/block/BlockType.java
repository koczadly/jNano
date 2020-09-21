/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;

import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;

/**
 * This class represents the types of block available.
 */
@JsonAdapter(BlockType.Adapter.class)
public enum BlockType {
    
    OPEN    (true,  OpenBlock.class,    OpenBlock.DESERIALIZER),
    CHANGE  (false, ChangeBlock.class,  ChangeBlock.DESERIALIZER),
    SEND    (true,  SendBlock.class,    SendBlock.DESERIALIZER),
    RECEIVE (true,  ReceiveBlock.class, ReceiveBlock.DESERIALIZER),
    STATE   (true,  StateBlock.class,   StateBlock.DESERIALIZER, "utx");
    
    
    private static final Map<String, BlockType> LOOKUP_MAP = new HashMap<>();
    
    static {
        for (BlockType type : BlockType.values()) {
            LOOKUP_MAP.put(type.getProtocolName().toLowerCase(), type);
            type.getAlternateNames().forEach(n -> LOOKUP_MAP.put(n.toLowerCase(), type));
        }
    }
    
    
    final boolean isTransaction;
    final Class<? extends Block> blockClass;
    final Function<JsonObject, ? extends Block> deserializer;
    final Set<String> altNames;
    
    BlockType(boolean isTransaction, Class<? extends Block> blockClass,
              Function<JsonObject, ? extends Block> deserializer, String...altNames) {
        this.isTransaction = isTransaction;
        this.blockClass = blockClass;
        this.deserializer = deserializer;
        this.altNames = Collections.unmodifiableSet(new HashSet<>(List.of(altNames)));
    }
    
    
    /**
     * @return whether the block type represents a transfer of funds
     * @deprecated Certain block types require contextual information to determine the action. This will return
     * {@code true} for state blocks, but this is not necessarily the case.
     */
    @Deprecated
    public boolean isTransaction() {
        return isTransaction;
    }
    
    /**
     * @return the class associated with the block type
     */
    public Class<? extends Block> getBlockClass() {
        return blockClass;
    }
    
    /**
     * @return the official protocol name of this subtype
     */
    public String getProtocolName() {
        return name().toLowerCase();
    }
    
    /**
     * @return a list of alternate protocol names
     */
    public Set<String> getAlternateNames() {
        return altNames;
    }
    
    /**
     * @return a function which converts a {@link JsonObject} into the appropriate {@link Block} instance
     */
    public Function<JsonObject, ? extends Block> getDeserializerFunction() {
        return deserializer;
    }
    
    @Override
    public String toString() {
        return getProtocolName();
    }
    
    
    /**
     * Returns the enum constant of this type from the protocol name.
     *
     * @param name the type name
     * @return the corresponding type, or null if not found
     */
    public static BlockType fromName(String name) {
        return LOOKUP_MAP.get(name.toLowerCase());
    }
    
    
    
    static class Adapter implements JsonSerializer<BlockType>, JsonDeserializer<BlockType> {
        @Override
        public BlockType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return fromName(json.getAsString());
        }
    
        @Override
        public JsonElement serialize(BlockType src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getProtocolName());
        }
    }
    
}
