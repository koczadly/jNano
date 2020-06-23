package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * This class can be used to convert raw JSON objects into the appropriate block objects.
 */
public final class BlockDeserializer {
    
    private Map<String, Function<JsonObject, ? extends Block>> deserializers = new ConcurrentHashMap<>();
    
    public BlockDeserializer() {
        // STATE
        Function<JsonObject, Block> stateDeserializer = json -> new StateBlock(
                json,
                nullableJsonObj(json.get("subtype"), o -> StateBlockSubType.getFromName(o.getAsString())),
                nullableJsonObj(json.get("hash"), JsonElement::getAsString),
                json.get("signature").getAsString(),
                nullableJsonObj(json.get("work"), o -> new WorkSolution(o.getAsString())),
                NanoAccount.parse(json.get("account").getAsString()),
                json.get("previous").getAsString(),
                NanoAccount.parse(json.get("representative").getAsString()),
                json.get("balance").getAsBigInteger(),
                nullableJsonObj(json.get("link"), JsonElement::getAsString),
                nullableJsonObj(json.get("link_as_account"), o -> NanoAccount.parse(o.getAsString()))
        );
        registerDeserializer("state", stateDeserializer);
        registerDeserializer("utx", stateDeserializer);
        
        // CHANGE
        registerDeserializer("change", json -> new ChangeBlock(
                json,
                nullableJsonObj(json.get("hash"), JsonElement::getAsString),
                json.get("signature").getAsString(),
                nullableJsonObj(json.get("work"), o -> new WorkSolution(o.getAsString())),
                json.get("previous").getAsString(),
                NanoAccount.parse(json.get("representative").getAsString())
        ));
        
        // OPEN
        registerDeserializer("open", json -> new OpenBlock(
                json,
                nullableJsonObj(json.get("hash"), JsonElement::getAsString),
                json.get("signature").getAsString(),
                nullableJsonObj(json.get("work"), o -> new WorkSolution(o.getAsString())),
                json.get("source").getAsString(),
                NanoAccount.parse(json.get("account").getAsString()),
                NanoAccount.parse(json.get("representative").getAsString())
        ));
        
        // RECEIVE
        registerDeserializer("receive", json -> new ReceiveBlock(
                json,
                nullableJsonObj(json.get("hash"), JsonElement::getAsString),
                json.get("signature").getAsString(),
                nullableJsonObj(json.get("work"), o -> new WorkSolution(o.getAsString())),
                json.get("previous").getAsString(),
                json.get("source").getAsString())
        );
        
        // SEND
        registerDeserializer("send", json -> new SendBlock(
                json,
                nullableJsonObj(json.get("hash"), JsonElement::getAsString),
                json.get("signature").getAsString(),
                new WorkSolution(json.get("work").getAsString()),
                json.get("previous").getAsString(),
                NanoAccount.parse(json.get("destination").getAsString()),
                new BigInteger(json.get("balance").getAsString(), 16) // Hex encoded value
        ));
    }
    
    
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
     * <p>Converts a {@link JsonObject} into the appropriate {@link Block} object types.</p>
     * <p>Supported block types include: {@link StateBlock}, {@link SendBlock}, {@link ReceiveBlock}, {@link OpenBlock}
     * and {@link ChangeBlock}.</p>
     *
     * @param jsonObj the JSON object
     * @return the deserialized block object
     */
    @SuppressWarnings("deprecation")
    public Block deserialize(JsonObject jsonObj) {
        String blockType = jsonObj.get("type").getAsString();
        Function<JsonObject, ? extends Block> deserializer = getDeserializer(blockType);
        if (deserializer == null)
            throw new JsonParseException("Block type " + blockType + " is invalid");
        return deserializer.apply(jsonObj);
    }
    
    
    private static <T> T nullableJsonObj(JsonElement obj, Function<JsonElement, T> func) {
        return obj != null ? func.apply(obj) : null;
    }
    
}
