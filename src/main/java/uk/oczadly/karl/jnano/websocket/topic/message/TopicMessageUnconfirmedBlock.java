package uk.oczadly.karl.jnano.websocket.topic.message;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.JsonAdapter;
import uk.oczadly.karl.jnano.model.block.Block;

import java.lang.reflect.Type;

@JsonAdapter(TopicMessageUnconfirmedBlock.Deserializer.class)
public class TopicMessageUnconfirmedBlock {
    
    private Block block;
    
    public Block getBlock() {
        return block;
    }
    
    
    static class Deserializer implements JsonDeserializer<TopicMessageUnconfirmedBlock> {
        @Override
        public TopicMessageUnconfirmedBlock deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            TopicMessageUnconfirmedBlock o = new TopicMessageUnconfirmedBlock();
            o.block = context.deserialize(json, Block.class);
            return o;
        }
    }
    
}
