/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * This class can be used to convert raw JSON objects into the appropriate block objects.
 */
public final class BlockDeserializer {
    
    static final BlockDeserializer DEFAULT = BlockDeserializer.withDefaults();
    
    private final Map<String, Function<JsonObject, ? extends Block>> deserializers = new ConcurrentHashMap<>();
    
    private BlockDeserializer() {}
    
    
    public Map<String, Function<JsonObject, ? extends Block>> getDeserializers() {
        return Collections.unmodifiableMap(deserializers);
    }
    
    
    /**
     * Registers the supplied deserializer function to the given block type name.
     * @param blockType the block type name to register
     * @param deserializer the deserializer function
     */
    public void registerDeserializer(String blockType, Function<JsonObject, ? extends Block> deserializer) {
        deserializers.put(blockType.toLowerCase(), deserializer);
    }
    
    /**
     * Registers the supplied deserializer function to the given block type.
     * @param blockType the block type to register
     * @param deserializer the deserializer function
     */
    public void registerDeserializer(BlockType blockType, Function<JsonObject, ? extends Block> deserializer) {
        registerDeserializer(blockType.getProtocolName(), deserializer);
        blockType.getAlternateNames().forEach(n -> registerDeserializer(n, deserializer));
    }
    
    /**
     * Registers the block type to the default deserializer for the given type.
     * @param blockType the block type to register
     * @see BlockType#getDeserializerFunction()
     */
    public void registerDeserializer(BlockType blockType) {
        registerDeserializer(blockType, blockType.getDeserializerFunction());
    }
    
    
    /**
     * Returns the deserializer function associated with the block type name.
     * @param blockType the block type name
     * @return the associated deserializer function, or null if not found
     */
    public Function<JsonObject, ? extends Block> getDeserializer(String blockType) {
        return deserializers.get(blockType.toLowerCase());
    }
    
    /**
     * Returns the deserializer function associated with the block type.
     * @param blockType the block type
     * @return the associated deserializer function, or null if not found
     */
    public Function<JsonObject, ? extends Block> getDeserializer(BlockType blockType) {
        return getDeserializer(blockType.getProtocolName());
    }
    
    
    /**
     * <p>Converts a {@link JsonObject} into the appropriate {@link Block} object types, based on the internal
     * registry. To support additional block types, use {@link #registerDeserializer(String, Function)}.</p>
     *
     * @param jsonObj the JSON object
     * @return the deserialized block object
     * @throws BlockParseException if the block could not be parsed
     */
    public Block deserialize(JsonObject jsonObj) {
        if (!jsonObj.has("type"))
            throw new BlockParseException("No block type is specified.");
        
        String blockType = jsonObj.get("type").getAsString().toLowerCase();
        
        Function<JsonObject, ? extends Block> deserializer = getDeserializer(blockType);
        if (deserializer != null) {
            try {
                return deserializer.apply(jsonObj);
            } catch (Exception e) {
                throw new BlockParseException("Could not parse block (malformed json?).", e);
            }
        }
        throw new BlockParseException("Block type \"" + blockType + "\" is not supported by the deserializer.");
    }
    
    
    /**
     * Constructs a new BlockDeserializer with the default supported block deserializers.
     * <p>The following types are supported by default: {@link StateBlock state}, {@link ChangeBlock change},
     * {@link OpenBlock open}, {@link ReceiveBlock receive}, {@link SendBlock send}.</p>
     * @return a newly created BlockDeserializer object
     */
    public static BlockDeserializer withDefaults() {
        BlockDeserializer deserializer = new BlockDeserializer();
        
        deserializer.registerDeserializer(BlockType.STATE);
        deserializer.registerDeserializer(BlockType.CHANGE);
        deserializer.registerDeserializer(BlockType.OPEN);
        deserializer.registerDeserializer(BlockType.RECEIVE);
        deserializer.registerDeserializer(BlockType.SEND);
        return deserializer;
    }
    
    /**
     * Constructs a new BlockDeserializer without any of the default block deserializers. You will need to register
     * new deserializers for this to work.
     * @return a newly created BlockDeserializer object
     */
    public static BlockDeserializer withNone() {
        return new BlockDeserializer();
    }
    
    
    
    /**
     * A Gson adapter ({@link JsonSerializer} and {@link JsonDeserializer}) which allows the serialization and
     * deserialization of blocks.
     */
    public static class JsonAdapter implements JsonSerializer<Block>, JsonDeserializer<Block> {
        
        private final BlockDeserializer deserializer;
        
        /** With the default block deserializer. */
        public JsonAdapter() {
            this(DEFAULT);
        }
    
        /**
         * With the specified block deserializer.
         * @param deserializer the block deserializer
         */
        public JsonAdapter(BlockDeserializer deserializer) {
            this.deserializer = deserializer;
        }
        
        
        @Override
        public JsonElement serialize(Block src, Type typeOfSrc, JsonSerializationContext context) {
            return src.toJsonObject();
        }
        
        @Override
        public Block deserialize(JsonElement element, Type type, JsonDeserializationContext context)
                throws JsonParseException {
            // Check if string — sometimes blocks are passed as string representations
            JsonObject jsonObj = element.isJsonObject()
                    ? element.getAsJsonObject()
                    : JsonParser.parseString(element.getAsString()).getAsJsonObject();
            return deserializer.deserialize(jsonObj);
        }
        
    }
    
    
    public static final class BlockParseException extends JsonParseException {
        public BlockParseException(String msg) {
            super(msg);
        }
        
        public BlockParseException(String msg, Throwable cause) {
            super(msg, cause);
        }
        
        public BlockParseException(Throwable cause) {
            super(cause);
        }
    }
    
}
