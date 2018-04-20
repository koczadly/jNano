package uk.oczadly.karl.jnano.rpc.adapters;

import com.google.gson.*;

import java.lang.reflect.Type;

public class BooleanTypeDeserializer implements JsonDeserializer<Boolean> {
    
    @Override
    public Boolean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String str = jsonElement.getAsString().toLowerCase();
        return str.equals("1") || str.equals("true");
    }
    
}
