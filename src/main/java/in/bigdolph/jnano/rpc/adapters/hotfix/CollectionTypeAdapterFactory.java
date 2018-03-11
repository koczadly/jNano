package in.bigdolph.jnano.rpc.adapters.hotfix;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/** This adapter has to be implemented as the Nano RPC client returns blank strings rather than an empty array. */
public class CollectionTypeAdapterFactory implements TypeAdapterFactory {
    
    private final ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
    
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Class<? super T> rawType = typeToken.getRawType();
        if(!Collection.class.isAssignableFrom(rawType)) return null;
        
        TypeAdapter<T> delegateAdapter = gson.getDelegateAdapter(this, typeToken);
        if(delegateAdapter == null) return null;
        
        return new Adapter(delegateAdapter, constructor.get(typeToken));
    }
    
    
    
    private static class Adapter<E> extends TypeAdapter<Collection<E>> {
        
        private final TypeAdapter<Collection<E>> delegateAdapter;
        private final ObjectConstructor<? extends Collection<E>> constructor;
        
        public Adapter(TypeAdapter<Collection<E>> standardAdapter, ObjectConstructor<? extends Collection<E>> constructor) {
            this.delegateAdapter = standardAdapter;
            this.constructor = constructor;
        }
        
        
        
        @Override
        public Collection<E> read(JsonReader in) throws IOException {
            if(in.peek() == JsonToken.STRING) {
                if(in.nextString().length() == 0) return constructor.construct();
                throw new JsonSyntaxException("Parsed hotfix string is not empty");
            }
            return this.delegateAdapter.read(in); //Standard read operation
        }
        
        @Override
        public void write(JsonWriter out, Collection<E> obj) throws IOException {
            this.delegateAdapter.write(out, obj); //Standard write operation
        }
        
    }
    
}
