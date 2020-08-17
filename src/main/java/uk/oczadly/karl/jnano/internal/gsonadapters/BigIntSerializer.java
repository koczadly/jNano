package uk.oczadly.karl.jnano.internal.gsonadapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.math.BigInteger;

public class BigIntSerializer implements JsonSerializer<BigInteger> {
    
    @Override
    public JsonElement serialize(BigInteger src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString()); // Cast as string instead of numeral
    }
    
}
