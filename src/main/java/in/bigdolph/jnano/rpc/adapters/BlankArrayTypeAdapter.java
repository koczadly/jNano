package in.bigdolph.jnano.rpc.adapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * This class has to be implemented as the Nano RPC client returns blank strings rather than an empty array.
 * It is based off the raw source code for ArrayTypeAdapter within Gson
 * */
public class BlankArrayTypeAdapter<E> extends TypeAdapter<Object> {
    
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            Type type = typeToken.getType();
            if (!(type instanceof GenericArrayType || type instanceof Class && ((Class<?>) type).isArray())) {
                return null;
            }
            
            Type componentType = $Gson$Types.getArrayComponentType(type);
            TypeAdapter<?> componentTypeAdapter = gson.getAdapter(TypeToken.get(componentType));
            return new BlankArrayTypeAdapter(gson, $Gson$Types.getRawType(componentType), typeToken, componentTypeAdapter);
        }
    };
    
    
    private final Gson gson;
    private final Class<E> componentType;
    private final TypeToken<E> typeToken;
    private final TypeAdapter<E> componentTypeAdapter;
    private final ArrayTypeAdapter<E> standardAdapter;
    
    public BlankArrayTypeAdapter(Gson gson, Class<E> componentType, TypeToken<E> typeToken, TypeAdapter<E> componentTypeAdapter) {
        this.gson = gson;
        this.componentType = componentType;
        this.typeToken = typeToken;
        this.componentTypeAdapter = componentTypeAdapter;
        this.standardAdapter = new ArrayTypeAdapter(gson, componentTypeAdapter, componentType);
    }
    
    
    
    @Override
    public Object read(JsonReader in) throws IOException {
        if(in.peek() == JsonToken.STRING) {
            in.nextString();
            return Array.newInstance(componentType, 0); //Return empty array
        }
        return this.standardAdapter.read(in); //Standard read operation
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void write(JsonWriter out, Object array) throws IOException {
        this.standardAdapter.write(out, array); //Standard write operation
    }
    
}
