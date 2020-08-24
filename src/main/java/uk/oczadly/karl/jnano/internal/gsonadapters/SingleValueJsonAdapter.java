package uk.oczadly.karl.jnano.internal.gsonadapters;

import com.google.gson.*;
import com.google.gson.annotations.Expose;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SingleValueJsonAdapter implements JsonDeserializer<Object> {
    
    private static Map<Class<?>, Field> FIELD_MAP = new ConcurrentHashMap<>();
    
    
    @SuppressWarnings("deprecation")
    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (!(typeOfT instanceof Class<?>))
            throw new JsonParseException("Destination type was not a class.");
        
        // Determine JSON element
        JsonObject jsonObj = json.getAsJsonObject();
        Set<String> jsonKeys = jsonObj.keySet();
        JsonElement jsonField = null;
        if (jsonKeys.size() > 1) {
            throw new JsonParseException("JSON object has more than 1 key (cannot deserialize into a " +
                    "SingleValueJsonAdapter).");
        } else if (jsonKeys.size() == 1) {
            jsonField = jsonObj.get(jsonKeys.iterator().next());
        }
        
        try {
            Class<?> cl = (Class<?>)typeOfT;
            
            Field containerField = FIELD_MAP.get(cl);
            if (containerField == null) {
                Field[] fields = cl.getDeclaredFields();
                for (Field f : fields) {
                    if (f.isAnnotationPresent(Expose.class)) {
                        if (containerField != null)
                            throw new JsonParseException("More than 1 field declared while using " +
                                    "SingleValueJsonAdapter.");
                        containerField = f;
                    }
                }
                if (containerField == null)
                    throw new JsonParseException("No exposed field was present within the destination class.");
                containerField.setAccessible(true);
                FIELD_MAP.put(cl, containerField);
            }
            
            Object o = cl.newInstance();
            if (jsonField != null)
                containerField.set(o, context.deserialize(jsonField, containerField.getType()));
            return o;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new JsonParseException(e);
        }
    }
    
}
