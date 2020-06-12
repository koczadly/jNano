package uk.oczadly.karl.jnano.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.oczadly.karl.jnano.internal.gsonadapters.ArrayTypeAdapterFactoryFix;
import uk.oczadly.karl.jnano.internal.gsonadapters.BooleanTypeDeserializer;
import uk.oczadly.karl.jnano.internal.utils.BaseEncoder;

public class JNanoHelper {
    
    public static final char[] HEX_CHARS_UC = "0123456789ABCDEF".toCharArray();
    
    public static final BaseEncoder ENCODER_HEX = new BaseEncoder(HEX_CHARS_UC);
    public static final BaseEncoder ENCODER_NANO_B32 = new BaseEncoder("13456789abcdefghijkmnopqrstuwxyz");
    
    
    public static final Gson GSON = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapterFactory(new ArrayTypeAdapterFactoryFix())       // Empty array hotfix
            .registerTypeAdapter(boolean.class, new BooleanTypeDeserializer())  // Boolean deserializer
            .registerTypeAdapter(Boolean.class, new BooleanTypeDeserializer())  // Boolean deserializer
            .create();
    
}
