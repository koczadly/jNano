/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc;

import com.google.gson.*;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.rpc.exception.*;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;

import java.lang.reflect.Field;

/**
 * Default implementation of {@link RpcResponseDeserializer}.
 */
public class RpcResponseDeserializerImpl implements RpcResponseDeserializer {
    
    private static volatile Field RESPONSE_JSON_FIELD;
    
    private final Gson gson;
    
    
    public RpcResponseDeserializerImpl() {
        this(JNH.GSON);
    }
    
    public RpcResponseDeserializerImpl(Gson gson) {
        this.gson = gson;
    }
    
    
    public Gson getGsonInstance() {
        return gson;
    }
    
    
    @Override
    @SuppressWarnings("unchecked")
    public <R extends RpcResponse> R deserialize(String response, Class<R> responseClass) throws RpcException {
        try {
            // Parse response into JSON
            JsonObject responseJson = JsonParser.parseString(response).getAsJsonObject();
            
            // Check for returned RPC error
            JsonElement errorElement = responseJson.get("error");
            if (errorElement != null) {
                String errorStr = errorElement.getAsString();
                if (responseClass == ResponseSuccessful.class && errorStr.equals("Empty response")) {
                    // Fix for empty response error
                    return (R)new ResponseSuccessful(true);
                } else {
                    throw parseException(errorStr);
                }
            }
    
            // Deserialize response
            R responseObj = gson.fromJson(responseJson, responseClass);
            populateJsonField(responseObj, responseJson);
    
            return responseObj;
        } catch (JsonParseException ex) {
            throw new RpcInvalidResponseException(response, ex); // If unable to parse
        }
    }
    
    
    /**
     * Parses an {@link RpcException} from a given response message.
     * @param msg the received error message
     * @return the parsed exception object
     * @deprecated Method may be changed or removed in the future.
     */
    @Deprecated
    public static RpcException parseException(String msg) {
        String msgLc = msg.toLowerCase().trim();
        
        // Check and parse error type
        switch (msgLc) {
            case "wallet is locked":
            case "wallet locked":
                return new RpcWalletLockedException();             // Wallet locked
            case "insufficient balance":
                return new RpcInvalidArgumentException(msg + "."); // Invalid/bad argument
            case "rpc control is disabled":
                return new RpcControlDisabledException();          // RPC control disabled
            case "cancelled":
                return new RpcRequestCancelledException();         // Request cancelled
            case "unable to parse json":
                return new RpcInvalidRequestJsonException(         // Invalid request body
                        "The RPC server was unable to parse the JSON request.");
            case "unknown command":
                return new RpcUnknownCommandException();           // Unknown command
            case "invalid header: body limit exceeded":
                return new RpcInvalidRequestJsonException(         // JSON too long
                        "The request JSON exceeded the configured maximum length.");
            case "unsafe rpc not allowed":
                return new RpcUnsafeNotAllowedException();         // RPC unsafe
        }
        
        if (msgLc.startsWith("bad") || msgLc.startsWith("invalid") || msgLc.endsWith("invalid")
                || msgLc.endsWith("required")) {
            return new RpcInvalidArgumentException(msg + ".");    // Invalid/bad argument
        } else if (msgLc.contains("not found")) {
            return new RpcEntityNotFoundException(msg + ".");     // Unknown referenced entity
        } else if (msgLc.endsWith("is disabled")) {
            return new RpcFeatureDisabledException(msg + ".");    // Feature is disabled
        } else if (msgLc.contains("config")) {
            return new RpcConfigForbiddenException(msg + ".");    // Config forbids request
        } else if (msgLc.contains("json")) {
            return new RpcInvalidRequestJsonException(msg + "."); // Disallowed/invalid JSON request
        } else if (msgLc.startsWith("internal")) {
            return new RpcInternalException(msg + ".");           // Internal server error
        }
        
        return new RpcException(msg.isEmpty() ? null : (msg + ".")); // Default to base exception
    }
    
    
    /**
     * Populates the raw JSON field within {@link RpcResponse} objects.
     * @param response the response object
     * @param json     the raw JSON object
     * @deprecated Method may be changed or removed in the future.
     */
    @Deprecated
    public static void populateJsonField(RpcResponse response, JsonObject json) {
        if (RESPONSE_JSON_FIELD == null) {
            try {
                RESPONSE_JSON_FIELD = RpcResponse.class.getDeclaredField("rawJson");
                RESPONSE_JSON_FIELD.setAccessible(true);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        if (RESPONSE_JSON_FIELD != null) {
            try {
                if (RESPONSE_JSON_FIELD.get(response) != null)
                    throw new IllegalStateException("Response JSON value is already assigned.");
                
                RESPONSE_JSON_FIELD.set(response, json);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
