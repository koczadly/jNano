package uk.oczadly.karl.jnano.gsonadapters;

import com.google.gson.*;
import uk.oczadly.karl.jnano.model.block.BlockType;
import uk.oczadly.karl.jnano.callback.BlockInfo;
import uk.oczadly.karl.jnano.model.block.Block;

import java.lang.reflect.Type;

public class CallbackBlockTypeDeserializer implements JsonDeserializer<BlockInfo> {
    
    private JsonParser parser = new JsonParser();
    
    
    @Override
    public BlockInfo deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject json = element.getAsJsonObject();
        
        //Deserialize block
        Block block = context.deserialize(json.get("block"), Block.class);
        
        //Construct and return
        return new BlockInfo(
                element.toString(),
                json.get("account").getAsString(),
                json.get("hash").getAsString(),
                block,
                json.has("is_send") ? json.get("is_send").getAsBoolean() : block.getType() == BlockType.SEND, //UTX state block
                block.getType().isTransaction() ? json.get("amount").getAsBigInteger() : null); //Null if non-transactional
    }
    
}
