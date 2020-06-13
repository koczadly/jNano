package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import uk.oczadly.karl.jnano.model.AccountAddress;

import java.math.BigInteger;
import java.util.function.Function;

/**
 * This class can be used to convert raw JSON objects into the appropriate block objects.
 */
public final class BlockDeserializer {
    
    
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
        String hash = nullableJsonObj(jsonObj.get("hash"), JsonElement::getAsString);
        String blockType = jsonObj.get("type").getAsString().toUpperCase();
        
        switch (blockType) {
            case "UTX": // Old universal blocks type name
            case "STATE":
                return new StateBlock(
                        jsonObj,
                        nullableJsonObj(jsonObj.get("subtype"), o -> StateBlockSubType.getFromName(o.getAsString())),
                        hash,
                        jsonObj.get("signature").getAsString(),
                        jsonObj.get("work").getAsString(),
                        AccountAddress.parse(jsonObj.get("account").getAsString()),
                        jsonObj.get("previous").getAsString(),
                        AccountAddress.parse(jsonObj.get("representative").getAsString()),
                        jsonObj.get("balance").getAsBigInteger(),
                        nullableJsonObj(jsonObj.get("link"), JsonElement::getAsString),
                        nullableJsonObj(jsonObj.get("link_as_account"),
                                o -> AccountAddress.parse(o.getAsString())));
            case "OPEN":
                return new OpenBlock(
                        jsonObj,
                        hash,
                        jsonObj.get("signature").getAsString(),
                        jsonObj.get("work").getAsString(),
                        jsonObj.get("source").getAsString(),
                        jsonObj.get("account").getAsString(),
                        jsonObj.get("representative").getAsString());
            case "RECEIVE":
                return new ReceiveBlock(
                        jsonObj,
                        hash,
                        jsonObj.get("signature").getAsString(),
                        jsonObj.get("work").getAsString(),
                        jsonObj.get("previous").getAsString(),
                        jsonObj.get("source").getAsString());
            case "SEND":
                return new SendBlock(
                        jsonObj,
                        hash,
                        jsonObj.get("signature").getAsString(),
                        jsonObj.get("work").getAsString(),
                        jsonObj.get("previous").getAsString(),
                        jsonObj.get("destination").getAsString(),
                        new BigInteger(jsonObj.get("balance").getAsString(), 16)); // Hex encoded value
            case "CHANGE":
                return new ChangeBlock(
                        jsonObj,
                        hash,
                        jsonObj.get("signature").getAsString(),
                        jsonObj.get("work").getAsString(),
                        jsonObj.get("previous").getAsString(),
                        jsonObj.get("representative").getAsString());
            default:
                throw new JsonParseException("Block type " + blockType + " is invalid");
        }
    }
    
    
    private static <T> T nullableJsonObj(JsonElement obj, Function<JsonElement, T> func) {
        return obj != null ? func.apply(obj) : null;
    }
    
}
