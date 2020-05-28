package uk.oczadly.karl.jnano.rpc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import uk.oczadly.karl.jnano.internal.JNanoHelper;
import uk.oczadly.karl.jnano.rpc.exception.*;
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;

import java.lang.reflect.Field;

public class RpcResponseDeserializerImpl implements RpcResponseDeserializer {
    
    private static volatile Field RESPONSE_JSON_FIELD;
    
    static {
        try {
            RESPONSE_JSON_FIELD = RpcResponse.class.getDeclaredField("rawJson");
            RESPONSE_JSON_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
    
    
    @Override
    public <R extends RpcResponse> R deserialize(String response, Class<R> responseClass) throws RpcException {
        JsonObject responseJson;
        try {
            responseJson = JsonParser.parseString(response).getAsJsonObject(); // Parse response
        } catch (JsonSyntaxException ex) {
            throw new RpcInvalidResponseException(response, ex); // If unable to parse
        }
    
        // Check for returned RPC error
        JsonElement errorElement = responseJson.get("error");
        if (errorElement != null)
            throw parseException(errorElement.getAsString());
        
        // Deserialize response
        R responseObj = JNanoHelper.GSON.fromJson(responseJson, responseClass); // Deserialize from JSON
        
        // Populate original json object
        populateJsonField(responseObj, responseJson);
        
        return responseObj;
    }
    
    
    public RpcException parseException(String msg) {
        String msgLc = msg.toLowerCase();
        
        // Check and parse error type
        switch (msgLc) {
            case "wallet is locked":
            case "wallet locked":
                return new RpcWalletLockedException();             // Wallet locked
            case "insufficient balance":
                return new RpcInvalidArgumentException(msg + "."); // Invalid/bad argument
            case "invalid authorization header":
                return new RpcInvalidAuthTokenException();         // Invalid auth token
            case "rpc control is disabled":
                return new RpcControlDisabledException();          // RPC control disabled
            case "unable to parse json":
                return new RpcInvalidRequestJsonException();       // Invalid request body
            case "unknown command":
                return new RpcUnknownCommandException();           // Unknown command
        }
        
        if (msgLc.startsWith("bad") || msgLc.startsWith("invalid") || msgLc.endsWith("invalid")
                || msgLc.endsWith("required")) {
            return new RpcInvalidArgumentException(msg + ".");    // Invalid/bad argument
        } else if (msgLc.contains("not found")) {
            return new RpcEntityNotFoundException(msg + ".");     // Unknown referenced entity
        } else if (msgLc.endsWith("is disabled")) {
            return new RpcFeatureDisabledException(msg + ".");    // Feature is disabled
        } else if (msgLc.startsWith("internal")) {
            return new RpcInternalException(msg + ".");           // Internal server error
        }
        
        return new RpcException(msg.isEmpty() ? null : (msg + ".")); // Default to base exception
    }
    
    
    private void populateJsonField(RpcResponse response, JsonObject json) {
        if (RESPONSE_JSON_FIELD != null) {
            try {
                RESPONSE_JSON_FIELD.set(response, json);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
