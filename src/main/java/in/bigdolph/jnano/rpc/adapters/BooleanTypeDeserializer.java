package in.bigdolph.jnano.rpc.adapters;

import com.google.gson.*;
import in.bigdolph.jnano.model.block.*;

import java.lang.reflect.Type;
import java.math.BigInteger;

public class BooleanTypeDeserializer implements JsonDeserializer<Boolean> {
    
    @Override
    public Boolean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String str = jsonElement.getAsString();
        return str.equals("1") || str.toLowerCase().equals("true");
    }
    
}
