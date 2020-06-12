package uk.oczadly.karl.jnano.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.oczadly.karl.jnano.internal.gsonadapters.ArrayTypeAdapterFactoryFix;
import uk.oczadly.karl.jnano.internal.gsonadapters.BooleanTypeDeserializer;

public class JNanoHelper {
    
    public static final char[] HEX_CHARS_UC = "0123456789ABCDEF".toCharArray();
    
    
    public static final Gson GSON = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapterFactory(new ArrayTypeAdapterFactoryFix())       // Empty array hotfix
            .registerTypeAdapter(boolean.class, new BooleanTypeDeserializer())  // Boolean deserializer
            .registerTypeAdapter(Boolean.class, new BooleanTypeDeserializer())  // Boolean deserializer
            .create();
    
}
