package uk.oczadly.karl.jnano.rpc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;

public class RpcRequestSerializerImpl implements RpcRequestSerializer {
    
    private Gson gson;
    
    public RpcRequestSerializerImpl() {
        this(JNH.GSON);
    }
    
    public RpcRequestSerializerImpl(Gson gson) {
        this.gson = gson;
    }
    
    
    public Gson getGsonInstance() {
        return gson;
    }
    
    
    @Override
    public String serialize(RpcRequest<?> request) {
        JsonObject obj = gson.toJsonTree(request).getAsJsonObject();
        obj.addProperty("action", request.getActionCommand());
        return obj.toString();
    }

}
