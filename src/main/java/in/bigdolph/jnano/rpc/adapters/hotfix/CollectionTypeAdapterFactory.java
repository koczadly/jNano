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
        
        return new Adapter(gson, delegateAdapter, constructor.get(typeToken));
    }
    
    
    private static class Adapter<E> extends TypeAdapter<Collection<E>> {
        
        private final Gson gson;
        private final TypeAdapter<Collection<E>> delegateAdapter;
        private final ObjectConstructor<? extends Collection<E>> constructor;
        
        public Adapter(Gson gson, TypeAdapter<Collection<E>> standardAdapter, ObjectConstructor<? extends Collection<E>> constructor) {
            this.gson = gson;
            this.delegateAdapter = standardAdapter;
            this.constructor = constructor;
        }
        
        
        @Override
        public Collection<E> read(JsonReader in) throws IOException {
            //From blank string
            if(in.peek() == JsonToken.STRING) {
                if(in.nextString().length() == 0) return constructor.construct();
                throw new JsonSyntaxException("Parsed hotfix string is not empty");
            }
            
            //From map keyset
            if(in.peek() == JsonToken.BEGIN_OBJECT) {
                //Read map
                Type type = new TypeToken<Map<E, ?>>(){}.getType();
                Map<E, ?> map = this.gson.fromJson(in, type);
                
                //Create collection
                Collection<E> col = constructor.construct();
                col.addAll(map.keySet());
                return col;
            }
            
            //Standard read operation
            return this.delegateAdapter.read(in);
        }
        
        @Override
        public void write(JsonWriter out, Collection<E> obj) throws IOException {
            this.delegateAdapter.write(out, obj); //Standard write operation
        }
        
    }
    
}
