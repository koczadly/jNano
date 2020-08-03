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
    
    private final Map<String, Function<JsonObject, ? extends Block>> deserializers = new ConcurrentHashMap<>();
    
    @SuppressWarnings("deprecation")
    public BlockDeserializer() {
        // STATE
        Function<JsonObject, Block> stateDeserializer = json -> new StateBlock(
                nullable(json.get("subtype"), o -> StateBlockSubType.getFromName(o.getAsString())),
                nullable(json.get("hash"), JsonElement::getAsString),
                json.get("signature").getAsString(),
                nullable(json.get("work"), o -> new WorkSolution(o.getAsString())),
                NanoAccount.parseAddress(json.get("account").getAsString()),
                json.get("previous").getAsString(),
                NanoAccount.parseAddress(json.get("representative").getAsString()),
                json.get("balance").getAsBigInteger(),
                nullable(json.get("link"), JsonElement::getAsString),
                nullable(json.get("link_as_account"), o -> NanoAccount.parseAddress(o.getAsString()))
        );
        registerDeserializer("state", stateDeserializer);
        registerDeserializer("utx", stateDeserializer);
        
        // CHANGE
        registerDeserializer("change", json -> new ChangeBlock(
                nullable(json.get("hash"), JsonElement::getAsString),
                json.get("signature").getAsString(),
                nullable(json.get("work"), o -> new WorkSolution(o.getAsString())),
                json.get("previous").getAsString(),
                NanoAccount.parseAddress(json.get("representative").getAsString())
        ));
        
        // OPEN
        registerDeserializer("open", json -> new OpenBlock(
                nullable(json.get("hash"), JsonElement::getAsString),
                json.get("signature").getAsString(),
                nullable(json.get("work"), o -> new WorkSolution(o.getAsString())),
                json.get("source").getAsString(),
                NanoAccount.parseAddress(json.get("account").getAsString()),
                NanoAccount.parseAddress(json.get("representative").getAsString())
        ));
        
        // RECEIVE
        registerDeserializer("receive", json -> new ReceiveBlock(
                nullable(json.get("hash"), JsonElement::getAsString),
                json.get("signature").getAsString(),
                nullable(json.get("work"), o -> new WorkSolution(o.getAsString())),
                json.get("previous").getAsString(),
                json.get("source").getAsString())
        );
        
        // SEND
        registerDeserializer("send", json -> new SendBlock(
                nullable(json.get("hash"), JsonElement::getAsString),
                json.get("signature").getAsString(),
                new WorkSolution(json.get("work").getAsString()),
                json.get("previous").getAsString(),
                NanoAccount.parseAddress(json.get("destination").getAsString()),
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
        
        throw new JsonParseException("Block type " + blockType + " is invalid");
    }
    
    
    private static <T, U> T nullable(U obj, Function<U, T> func) {
        return obj != null ? func.apply(obj) : null;
    }
    
}
