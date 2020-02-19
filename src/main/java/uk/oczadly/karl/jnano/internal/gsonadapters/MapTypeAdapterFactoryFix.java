package uk.oczadly.karl.jnano.internal.gsonadapters;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** This adapter has to be implemented as the Nano RPC client returns blank strings rather than an empty array. */
public class MapTypeAdapterFactoryFix implements TypeAdapterFactory {
    
    private final ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
    
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Class<? super T> rawType = typeToken.getRawType();
        if(!Map.class.isAssignableFrom(rawType)) return null;
        
        TypeAdapter<T> delegateAdapter = gson.getDelegateAdapter(this, typeToken);
        if(delegateAdapter == null) return null;
        
        return new Adapter(delegateAdapter, constructor.get(typeToken));
    }
    
    
    
    private static class Adapter<K, V> extends TypeAdapter<Map<K, V>> {
        
        private final TypeAdapter<Map<K, V>> delegateAdapter;
        private final ObjectConstructor<? extends Map<K, V>> constructor;
        
        public Adapter(TypeAdapter<Map<K, V>> standardAdapter, ObjectConstructor<? extends Map<K, V>> constructor) {
            this.delegateAdapter = standardAdapter;
            this.constructor = constructor;
        }
        
        
        
        @Override
        public Map<K, V> read(JsonReader in) throws IOException {
            if(in.peek() == JsonToken.STRING) {
                if(in.nextString().length() == 0) return constructor.construct();
                throw new JsonSyntaxException("Parsed hotfix string is not empty");
            }
            return this.delegateAdapter.read(in); // Standard read operation
        }
        
        @Override
        public void write(JsonWriter out, Map<K, V> obj) throws IOException {
            this.delegateAdapter.write(out, obj); // Standard write operation
        }
        
    }
    
}
