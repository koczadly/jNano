package in.bigdolph.jnano.rpc.adapters;

import com.google.gson.*;
import in.bigdolph.jnano.model.block.*;

import java.lang.reflect.Type;
import java.math.BigInteger;

public class BlockTypeDeserializer implements JsonDeserializer<Block> {
    
    private JsonParser parser = new JsonParser();
    
    
    @Override
    public Block deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObj = element.isJsonObject() ? element.getAsJsonObject() : this.parser.parse(element.getAsString()).getAsJsonObject(); //Sometimes blocks are passed as string representations
        String jsonStr = jsonObj.toString();
        
        String blockType = jsonObj.get("type").getAsString().toUpperCase();
        switch(blockType) {
            case "OPEN":
                return new BlockOpen(
                        jsonStr,
                        jsonObj.get("signature").getAsString(),
                        jsonObj.get("work").getAsString(),
                        jsonObj.get("source").getAsString(),
                        jsonObj.get("account").getAsString(),
                        jsonObj.get("representative").getAsString());
            case "RECEIVE":
                return new BlockReceive(
                        jsonStr,
                        jsonObj.get("signature").getAsString(),
                        jsonObj.get("work").getAsString(),
                        jsonObj.get("previous").getAsString(),
                        jsonObj.get("source").getAsString());
            case "SEND":
                return new BlockSend(
                        jsonStr,
                        jsonObj.get("signature").getAsString(),
                        jsonObj.get("work").getAsString(),
                        jsonObj.get("previous").getAsString(),
                        jsonObj.get("destination").getAsString(),
                        new BigInteger(jsonObj.get("balance").getAsString(), 16)); //Balance is encoded in hexadecimal
            case "CHANGE":
                return new BlockChange(
                        jsonStr,
                        jsonObj.get("signature").getAsString(),
                        jsonObj.get("work").getAsString(),
                        jsonObj.get("previous").getAsString(),
                        jsonObj.get("representative").getAsString());
            case "UTX":
            case "STATE":
                return new BlockState(
                        jsonStr,
                        jsonObj.get("signature").getAsString(),
                        jsonObj.get("work").getAsString(),
                        jsonObj.get("account").getAsString(),
                        jsonObj.get("previous").getAsString(),
                        jsonObj.get("representative").getAsString(),
                        new BigInteger(jsonObj.get("balance").getAsString(), 16), //TODO - balance might not be encoded
                        jsonObj.get("link").getAsString());
            default: throw new JsonParseException("Block type " + blockType + " does not exist");
        }
    }
    
}
