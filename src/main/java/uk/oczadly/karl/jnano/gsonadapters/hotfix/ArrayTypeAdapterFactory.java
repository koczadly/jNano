package uk.oczadly.karl.jnano.gsonadapters.hotfix;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

/** This adapter has to be implemented as the Nano RPC client returns blank strings rather than an empty array. */
public class ArrayTypeAdapterFactory implements TypeAdapterFactory {
    
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Type type = typeToken.getType();
        if(!((type instanceof GenericArrayType) || (type instanceof Class && ((Class<?>)type).isArray()))) return null;
    
        TypeAdapter<T> delegateAdapter = gson.getDelegateAdapter(this, typeToken);
        if(delegateAdapter == null) return null;
    
        Type componentType = $Gson$Types.getArrayComponentType(type);
        Class<?> componentTypeClass = $Gson$Types.getRawType(componentType);
        
        return new Adapter(delegateAdapter, componentTypeClass);
    }
    
    
    
    private static class Adapter<E> extends TypeAdapter<Object> {
        
        private final TypeAdapter<Object> delegateAdapter;
        private final Class<E> componentType;
        
        public Adapter(TypeAdapter<Object> standardAdapter, Class<E> componentType) {
            this.delegateAdapter = standardAdapter;
            this.componentType = componentType;
        }
        
        
        
        @Override
        public Object read(JsonReader in) throws IOException {
            if(in.peek() == JsonToken.STRING) {
                if(in.nextString().length() == 0) return Array.newInstance(componentType, 0);
                throw new JsonSyntaxException("Parsed hotfix string is not empty");
            }
            return this.delegateAdapter.read(in); //Standard read operation
        }
        
        @Override
        public void write(JsonWriter out, Object obj) throws IOException {
            this.delegateAdapter.write(out, obj); //Standard write operation
        }
        
    }
    
}
