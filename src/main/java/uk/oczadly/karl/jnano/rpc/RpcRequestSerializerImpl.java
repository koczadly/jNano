package uk.oczadly.karl.jnano.rpc;

import com.google.gson.Gson;
import uk.oczadly.karl.jnano.internal.JNanoHelper;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;

public class RpcRequestSerializerImpl implements RpcRequestSerializer {
    
    private Gson gson;
    
    public RpcRequestSerializerImpl() {
        this(JNanoHelper.GSON);
    }
    
    public RpcRequestSerializerImpl(Gson gson) {
        this.gson = gson;
    }
    
    
    public Gson getGsonInstance() {
        return gson;
    }
    
    
    @Override
    public String serialize(RpcRequest<?> request) {
        return gson.toJson(request);
    }

}
