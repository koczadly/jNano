package uk.oczadly.karl.jnano.internal.gsonadapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class BooleanTypeDeserializer implements JsonDeserializer<Boolean> {
    
    @Override
    public Boolean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String str = jsonElement.getAsString().toLowerCase();
        return str.equals("1") || str.equals("true");
    }
    
}
