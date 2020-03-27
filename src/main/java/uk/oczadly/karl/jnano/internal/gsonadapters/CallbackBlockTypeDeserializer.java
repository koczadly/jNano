package uk.oczadly.karl.jnano.internal.gsonadapters;

import com.google.gson.*;
import uk.oczadly.karl.jnano.callback.BlockData;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.BlockType;

import java.lang.reflect.Type;

public class CallbackBlockTypeDeserializer implements JsonDeserializer<BlockData> {
    
    @SuppressWarnings("deprecation")
    @Override
    public BlockData deserialize(JsonElement element, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject json = element.getAsJsonObject();
        
        // Deserialize block
        Block block = context.deserialize(json.get("block"), Block.class);
        
        // Construct and return
        return new BlockData(
                element.toString(),
                json.get("account").getAsString(),
                json.get("hash").getAsString(),
                block,
                context.deserialize(json.get("subtype"), BlockType.class),
                json.has("is_send")
                        ? json.get("is_send").getAsBoolean()
                        : block.getType() == BlockType.SEND, // UTX state block
                block.getType().isTransaction()
                        ? json.get("amount").getAsBigInteger()
                        : null); // Null if non-transactional
    }
    
}
