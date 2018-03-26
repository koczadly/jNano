package in.bigdolph.jnano.rpc.adapters;

import com.google.gson.*;
import in.bigdolph.jnano.model.block.*;
import in.bigdolph.jnano.rpc.callback.CallbackBlock;

import java.lang.reflect.Type;
import java.math.BigInteger;

public class CallbackBlockTypeDeserializer implements JsonDeserializer<CallbackBlock> {
    
    private JsonParser parser = new JsonParser();
    
    
    @Override
    public CallbackBlock deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject json = element.getAsJsonObject();
        
        //Deserialize block
        Block block = context.deserialize(json.get("block"), Block.class);
        
        //Construct and return
        return new CallbackBlock(
                element.toString(),
                json.get("account").getAsString(),
                json.get("hash").getAsString(),
                block,
                block.getType().isTransaction() ? json.get("amount").getAsBigInteger() : null); //Null if non-transactional
    }
    
}
