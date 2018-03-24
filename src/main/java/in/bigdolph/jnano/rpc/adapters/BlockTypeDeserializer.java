package in.bigdolph.jnano.rpc.adapters;

import com.google.gson.*;
import in.bigdolph.jnano.model.block.*;

import java.lang.reflect.Type;
import java.math.BigInteger;

public class BlockTypeDeserializer implements JsonDeserializer<Block> {
    
    private JsonParser parser = new JsonParser();
    
    
    @Override
    public Block deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject json = element.isJsonObject() ? element.getAsJsonObject() : this.parser.parse(element.getAsString()).getAsJsonObject(); //Sometimes blocks are passed as string representations
        
        String blockType = json.get("type").getAsString().toUpperCase();
        Block block = null;
        switch(blockType) {
            case "OPEN":
                block = new BlockOpen(
                        json.get("signature").getAsString(),
                        json.get("work").getAsString(),
                        json.get("source").getAsString(),
                        json.get("account").getAsString(),
                        json.get("representative").getAsString());
                break;
            case "RECEIVE":
                block = new BlockReceive(
                        json.get("signature").getAsString(),
                        json.get("work").getAsString(),
                        json.get("previous").getAsString(),
                        json.get("source").getAsString());
                break;
            case "SEND":
                block = new BlockSend(
                        json.get("signature").getAsString(),
                        json.get("work").getAsString(),
                        json.get("previous").getAsString(),
                        json.get("destination").getAsString(),
                        new BigInteger(json.get("balance").getAsString(), 16)); //Balance is encoded in hexadecimal
                break;
            case "CHANGE":
                block = new BlockChange(
                        json.get("signature").getAsString(),
                        json.get("work").getAsString(),
                        json.get("previous").getAsString(),
                        json.get("representative").getAsString());
                break;
        }
        if(block == null) throw new JsonParseException("Block type " + blockType + " does not exist");
        block.setJsonRepresentation(json.toString());
        return block;
    }
    
}
