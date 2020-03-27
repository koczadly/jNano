package uk.oczadly.karl.jnano.internal.gsonadapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/** This adapter has to be implemented as the Nano RPC client returns blank strings rather than an empty
 * arrays. This class will work for Arrays, Collections and Maps. */
public class ArrayTypeAdapterFactoryFix implements TypeAdapterFactory {
    
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Type type = typeToken.getType();
        Class<?> rawType = typeToken.getRawType();
    
        TypeAdapter<T> delegateAdapter = gson.getDelegateAdapter(this, typeToken);
        if(delegateAdapter != null) {
            if (type instanceof GenericArrayType || Collection.class.isAssignableFrom(rawType)
                    || Map.class.isAssignableFrom(rawType)) {
                return new Adapter<>(delegateAdapter);
            }
        }
        return null;
    }
    
    private static class Adapter<E> extends TypeAdapter<E> {
        private final TypeAdapter<E> delegateAdapter;
        
        public Adapter(TypeAdapter<E> standardAdapter) {
            this.delegateAdapter = standardAdapter;
        }
        
        
        @Override
        public E read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.STRING && in.nextString().length() == 0) {
                in = new JsonReader(new StringReader("[]")); // Treat as empty array
            }
            return this.delegateAdapter.read(in); // Standard read operation
        }
        
        @Override
        public void write(JsonWriter out, E obj) throws IOException {
            this.delegateAdapter.write(out, obj); // Standard write operation
        }
    }
    
}
