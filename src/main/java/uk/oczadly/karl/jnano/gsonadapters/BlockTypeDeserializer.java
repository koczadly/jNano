package uk.oczadly.karl.jnano.gsonadapters;

import com.google.gson.*;
import uk.oczadly.karl.jnano.model.block.*;

import java.lang.reflect.Type;
import java.math.BigInteger;

public class BlockTypeDeserializer implements JsonDeserializer<Block> {
    
    private JsonParser parser = new JsonParser();
    
    
    @Override
    public Block deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObj = element.isJsonObject() ? element.getAsJsonObject()
                : this.parser.parse(element.getAsString()).getAsJsonObject(); //Sometimes blocks are passed as string representations
        
        String hash = jsonObj.get("hash") != null ? jsonObj.get("hash").getAsString() : null;
        String blockType = jsonObj.get("type").getAsString().toUpperCase();
        switch(blockType) {
            case "UTX": //Old universal blocks type name
            case "STATE": return new StateBlock(
                    jsonObj,
                    hash,
                    jsonObj.get("signature").getAsString(),
                    jsonObj.get("work").getAsString(),
                    jsonObj.get("account").getAsString(),
                    jsonObj.get("previous").getAsString(),
                    jsonObj.get("representative").getAsString(),
                    jsonObj.get("balance").getAsBigInteger(),
                    jsonObj.get("link").getAsString(),
                    jsonObj.get("link_as_account").getAsString());
            case "OPEN": return new OpenBlock(
                    jsonObj,
                    hash,
                    jsonObj.get("signature").getAsString(),
                    jsonObj.get("work").getAsString(),
                    jsonObj.get("source").getAsString(),
                    jsonObj.get("account").getAsString(),
                    jsonObj.get("representative").getAsString());
            case "RECEIVE": return new ReceiveBlock(
                    jsonObj,
                    hash,
                    jsonObj.get("signature").getAsString(),
                    jsonObj.get("work").getAsString(),
                    jsonObj.get("previous").getAsString(),
                    jsonObj.get("source").getAsString());
            case "SEND": return new SendBlock(
                    jsonObj,
                    hash,
                    jsonObj.get("signature").getAsString(),
                    jsonObj.get("work").getAsString(),
                    jsonObj.get("previous").getAsString(),
                    jsonObj.get("destination").getAsString(),
                    new BigInteger(jsonObj.get("balance").getAsString(), 16)); //Balance is encoded in hexadecimal
            case "CHANGE": return new ChangeBlock(
                    jsonObj,
                    hash,
                    jsonObj.get("signature").getAsString(),
                    jsonObj.get("work").getAsString(),
                    jsonObj.get("previous").getAsString(),
                    jsonObj.get("representative").getAsString());
            default: throw new JsonParseException("Block type " + blockType + " is invalid");
        }
    }
    
}
