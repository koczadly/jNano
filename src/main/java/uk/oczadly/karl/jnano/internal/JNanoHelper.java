package uk.oczadly.karl.jnano.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import uk.oczadly.karl.jnano.internal.gsonadapters.ArrayTypeAdapterFactoryFix;
import uk.oczadly.karl.jnano.internal.gsonadapters.BooleanTypeDeserializer;
import uk.oczadly.karl.jnano.internal.gsonadapters.CollectionTypeAdapterFactoryFix;
import uk.oczadly.karl.jnano.internal.gsonadapters.MapTypeAdapterFactoryFix;

public class JNanoHelper {
    
    public static final JsonParser JSON_PARSER = new JsonParser();
    
    public static final Gson GSON = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapterFactory(new ArrayTypeAdapterFactoryFix())          //Empty array hotfix
            .registerTypeAdapterFactory(new CollectionTypeAdapterFactoryFix())     //Empty collection hotfix
            .registerTypeAdapterFactory(new MapTypeAdapterFactoryFix())            //Empty map hotfix
            .registerTypeAdapter(boolean.class, new BooleanTypeDeserializer())  //Boolean deserializer
            .registerTypeAdapter(Boolean.class, new BooleanTypeDeserializer())  //Boolean deserializer
            .create();
    
}
