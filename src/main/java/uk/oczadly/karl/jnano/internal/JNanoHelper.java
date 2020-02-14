package uk.oczadly.karl.jnano.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import uk.oczadly.karl.jnano.gsonadapters.BooleanTypeDeserializer;
import uk.oczadly.karl.jnano.gsonadapters.hotfix.ArrayTypeAdapterFactory;
import uk.oczadly.karl.jnano.gsonadapters.hotfix.CollectionTypeAdapterFactory;
import uk.oczadly.karl.jnano.gsonadapters.hotfix.MapTypeAdapterFactory;

public class JNanoHelper {
    
    public static final JsonParser JSON_PARSER = new JsonParser();
    
    public static final Gson GSON = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapterFactory(new ArrayTypeAdapterFactory())          //Empty array hotfix
            .registerTypeAdapterFactory(new CollectionTypeAdapterFactory())     //Empty collection hotfix
            .registerTypeAdapterFactory(new MapTypeAdapterFactory())            //Empty map hotfix
            .registerTypeAdapter(boolean.class, new BooleanTypeDeserializer())  //Boolean deserializer
            .registerTypeAdapter(Boolean.class, new BooleanTypeDeserializer())  //Boolean deserializer
            .create();
    
}
