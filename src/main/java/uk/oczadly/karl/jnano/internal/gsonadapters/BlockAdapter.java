package uk.oczadly.karl.jnano.internal.gsonadapters;

import com.google.gson.*;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.BlockDeserializer;

import java.lang.reflect.Type;

public class BlockAdapter implements JsonSerializer<Block>, JsonDeserializer<Block> {
    
    private static final BlockDeserializer blockDeserializer = new BlockDeserializer();
    
    
    @Override
    public JsonElement serialize(Block src, Type typeOfSrc, JsonSerializationContext context) {
        return src.getJsonObject();
    }
    
    @Override
    public Block deserialize(JsonElement element, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        // Check if string - sometimes blocks are passed as string representations
        JsonObject jsonObj = element.isJsonObject()
                ? element.getAsJsonObject()
                : JsonParser.parseString(element.getAsString()).getAsJsonObject();
        
        return blockDeserializer.deserialize(jsonObj);
    }
    
}
