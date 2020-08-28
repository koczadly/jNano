package uk.oczadly.karl.jnano.model.block;

import com.google.gson.*;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * This class can be used to convert raw JSON objects into the appropriate block objects.
 */
public final class BlockDeserializer {
    
    private final Map<String, Function<JsonObject, ? extends Block>> deserializers = new ConcurrentHashMap<>();
    
    private BlockDeserializer() {}
    
    
    public void registerDeserializer(String name, Function<JsonObject, ? extends Block> deserializer) {
        deserializers.put(name.toLowerCase(), deserializer);
    }
    
    public Map<String, Function<JsonObject, ? extends Block>> getDeserializers() {
        return deserializers;
    }
    
    public Function<JsonObject, ? extends Block> getDeserializer(String blockType) {
        return deserializers.get(blockType.toLowerCase());
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
        
        // STATE
        Function<JsonObject, Block> stateDeserializer = json -> new StateBlock(
                JNH.nullable(json.get("subtype"), o -> StateBlockSubType.getFromName(o.getAsString())),
                JNH.nullable(json.get("hash"), JsonElement::getAsString),
                json.get("signature").getAsString(),
                JNH.nullable(json.get("work"), o -> new WorkSolution(o.getAsString())),
                NanoAccount.parseAddress(json.has("account") ? json.get("account").getAsString() :
                        json.get("representative").getAsString()),
                json.get("previous").getAsString(),
                NanoAccount.parseAddress(json.get("representative").getAsString()),
                json.get("balance").getAsBigInteger(),
                JNH.nullable(json.get("link"), JsonElement::getAsString),
                JNH.nullable(json.get("link_as_account"), o -> NanoAccount.parseAddress(o.getAsString()))
        );
        deserializer.registerDeserializer("state", stateDeserializer);
        deserializer.registerDeserializer("utx", stateDeserializer);
    
        // CHANGE
        deserializer.registerDeserializer("change", json -> new ChangeBlock(
                JNH.nullable(json.get("hash"), JsonElement::getAsString),
                json.get("signature").getAsString(),
                JNH.nullable(json.get("work"), o -> new WorkSolution(o.getAsString())),
                json.get("previous").getAsString(),
                NanoAccount.parseAddress(json.get("representative").getAsString())
        ));
    
        // OPEN
        deserializer.registerDeserializer("open", json -> new OpenBlock(
                JNH.nullable(json.get("hash"), JsonElement::getAsString),
                json.get("signature").getAsString(),
                JNH.nullable(json.get("work"), o -> new WorkSolution(o.getAsString())),
                json.get("source").getAsString(),
                NanoAccount.parseAddress(json.get("account").getAsString()),
                NanoAccount.parseAddress(json.get("representative").getAsString())
        ));
    
        // RECEIVE
        deserializer.registerDeserializer("receive", json -> new ReceiveBlock(
                JNH.nullable(json.get("hash"), JsonElement::getAsString),
                json.get("signature").getAsString(),
                JNH.nullable(json.get("work"), o -> new WorkSolution(o.getAsString())),
                json.get("previous").getAsString(),
                json.get("source").getAsString())
        );
    
        // SEND
        deserializer.registerDeserializer("send", json -> new SendBlock(
                JNH.nullable(json.get("hash"), JsonElement::getAsString),
                json.get("signature").getAsString(),
                new WorkSolution(json.get("work").getAsString()),
                json.get("previous").getAsString(),
                NanoAccount.parseAddress(json.get("destination").getAsString()),
                new BigInteger(json.get("balance").getAsString())
        ));
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
