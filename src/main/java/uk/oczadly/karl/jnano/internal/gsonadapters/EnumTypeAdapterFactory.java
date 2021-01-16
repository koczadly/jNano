/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.internal.gsonadapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Case-insensitive enum type adapter. Serialization will return a lowercase variant.
 * */
public class EnumTypeAdapterFactory implements TypeAdapterFactory {
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType().isEnum()) {
            /*
             * We only want to override the default Gson EnumTypeAdapter implementation.
             * This is probably a piss-poor way of doing it, but hey, it works. We simply check that the adapter is
             * an inner class of the com.google.gson.internal.bind.TypeAdapters class, and if it is, override it with
             * our case-insensitive adapter implementation.
             */
            TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
            if (delegate.getClass().getName().startsWith("com.google.gson.internal.bind.TypeAdapters"))
                return new EnumTypeAdapter(type.getRawType());
        }
        return null;
    }
    
    /* Based on default GSON implementation */
    private static final class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {
        private final Map<String, T> nameToConstant = new HashMap<>();
        private final Map<T, String> constantToName = new HashMap<>();
        
        public EnumTypeAdapter(Class<T> classOfT) {
            try {
                for (T constant : classOfT.getEnumConstants()) {
                    String name = constant.name();
                    SerializedName annotation = classOfT.getField(name).getAnnotation(SerializedName.class);
                    if (annotation == null) {
                        name = name.toLowerCase();
                    } else {
                        name = annotation.value();
                        for (String alternate : annotation.alternate()) {
                            nameToConstant.put(alternate.toLowerCase(), constant);
                        }
                    }
                    nameToConstant.put(name.toLowerCase(), constant);
                    constantToName.put(constant, name);
                }
            } catch (NoSuchFieldException e) {
                throw new AssertionError(e);
            }
        }
        @Override public T read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return nameToConstant.get(in.nextString().toLowerCase());
        }
        
        @Override public void write(JsonWriter out, T value) throws IOException {
            out.value(value == null ? null : constantToName.get(value));
        }
    }
    
}
