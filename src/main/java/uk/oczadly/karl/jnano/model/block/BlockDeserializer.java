package uk.oczadly.karl.jnano.model.block;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * This class can be used to convert raw JSON objects into the appropriate block objects.
 */
public final class BlockDeserializer {
    
    private final Map<String, Function<JsonObject, ? extends Block>> deserializers = new ConcurrentHashMap<>();
    
    private BlockDeserializer() {}
    
    
    public Map<String, Function<JsonObject, ? extends Block>> getDeserializers() {
        return deserializers;
    }
    
    public void registerDeserializer(String blockType, Function<JsonObject, ? extends Block> deserializer) {
        deserializers.put(blockType.toLowerCase(), deserializer);
    }
    
    public void registerDeserializer(BlockType blockType, Function<JsonObject, ? extends Block> deserializer) {
        registerDeserializer(blockType.getProtocolName(), deserializer);
    }
    
    public Function<JsonObject, ? extends Block> getDeserializer(String blockType) {
        return deserializers.get(blockType.toLowerCase());
    }
    
    public Function<JsonObject, ? extends Block> getDeserializer(BlockType blockType) {
        return getDeserializer(blockType.getProtocolName());
    }
    
    
    /**
     * <p>Converts a {@link JsonObject} into the appropriate {@link Block} object types, based on the internal
     * registry. To support additional block types, use {@link #registerDeserializer(String, Function)}.</p>
     *
     * @param jsonObj the JSON object
     * @return the deserialized block object
     */
    @SuppressWarnings("deprecation")
    public Block deserialize(JsonObject jsonObj) {
        String blockType = jsonObj.get("type").getAsString();
        
        Function<JsonObject, ? extends Block> deserializer = getDeserializer(blockType);
        if (deserializer != null)
            return deserializer.apply(jsonObj);
        
        throw new JsonParseException("Block type " + blockType + " is not supported by the registered deserializer.");
    }
    
    
    /**
     * Constructs a new BlockDeserializer with the default supported block deserializers.
     * @return a newly created BlockDeserializer object
     */
    public static BlockDeserializer withDefaults() {
        BlockDeserializer deserializer = new BlockDeserializer();
        
        deserializer.registerDeserializer(BlockType.STATE,   StateBlock.DESERIALIZER);   // State
        deserializer.registerDeserializer("utx",             StateBlock.DESERIALIZER);   // State
        deserializer.registerDeserializer(BlockType.CHANGE,  ChangeBlock.DESERIALIZER);  // Change
        deserializer.registerDeserializer(BlockType.OPEN,    OpenBlock.DESERIALIZER);    // Open
        deserializer.registerDeserializer(BlockType.RECEIVE, ReceiveBlock.DESERIALIZER); // Receive
        deserializer.registerDeserializer(BlockType.SEND,    SendBlock.DESERIALIZER);    // Send
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
    public static final class JsonAdapter implements JsonSerializer<Block>, JsonDeserializer<Block> {
        
        private final BlockDeserializer deserializer;
        
        public JsonAdapter() {
            this(BlockDeserializer.withDefaults());
        }
        
        public JsonAdapter(BlockDeserializer deserializer) {
            this.deserializer = deserializer;
        }
        
        
        @Override
        public JsonElement serialize(Block src, Type typeOfSrc, JsonSerializationContext context) {
            return src.getJsonObject();
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
    
}
