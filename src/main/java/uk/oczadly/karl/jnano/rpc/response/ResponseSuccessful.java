package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;

import java.lang.reflect.Type;

@JsonAdapter(ResponseSuccessful.Deserializer.class)
public class ResponseSuccessful extends RpcResponse {
    
    private final boolean success;
    
    private ResponseSuccessful(boolean success) {
        this.success = success;
    }
    
    
    public boolean isSuccessful() {
        return success;
    }
    
    
    
    protected static class Deserializer implements JsonDeserializer<ResponseSuccessful> {
        @Override
        public ResponseSuccessful deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            
            if (json.isJsonPrimitive()) {
                return new ResponseSuccessful(json.getAsBoolean());
            } else if (json.isJsonObject()) {
                JsonObject obj = json.getAsJsonObject();
                
                if (obj.size() == 1) {
                    String singleVal = obj.entrySet().iterator().next().getValue().getAsString();
                    return new ResponseSuccessful(singleVal.equalsIgnoreCase("true") || singleVal.equals("1")
                            || singleVal.equals(""));
                }
                
                return new ResponseSuccessful(true);
            }
            return new ResponseSuccessful(false);
        }
    }
    
}
