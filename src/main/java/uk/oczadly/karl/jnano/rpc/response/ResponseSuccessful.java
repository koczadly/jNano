/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;

import java.lang.reflect.Type;

/**
 * This response class is a generic class used when a single boolean response is expected, or in some cases, where no
 * response is expected from the server.
 */
@JsonAdapter(ResponseSuccessful.Deserializer.class)
public class ResponseSuccessful extends RpcResponse {
    
    private final boolean success;
    
    private ResponseSuccessful(boolean success) {
        this.success = success;
    }
    
    
    /**
     * Note: depending on the query, the given value may have no function and could be incorrect.
     *
     * @return whether the query was successful
     */
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
                } else {
                    // More or less than 1 argument given
                    return new ResponseSuccessful(true);
                }
            }
            return new ResponseSuccessful(false); // Unknown response type
        }
    }
    
}
