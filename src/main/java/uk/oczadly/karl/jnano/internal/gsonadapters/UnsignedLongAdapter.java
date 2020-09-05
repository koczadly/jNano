/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.internal.gsonadapters;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * @author Karl Oczadly
 */
public class UnsignedLongAdapter implements JsonSerializer<Long>, JsonDeserializer<Long> {
    
    @Override
    public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return Long.parseUnsignedLong(json.getAsString());
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }
    
    @Override
    public JsonElement serialize(Long src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(Long.toUnsignedString(src));
    }
    
}
