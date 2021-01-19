/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import uk.oczadly.karl.jnano.internal.JNC;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.rpc.exception.*;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;

import java.lang.reflect.Field;

/**
 * The standard implementation of {@link RpcResponseDeserializer}, which deserializes the response as a JSON object.
 */
public class JsonResponseDeserializer implements RpcResponseDeserializer {
    
    private static volatile Field RESPONSE_JSON_FIELD;
    
    private final Gson gson;
    
    
    public JsonResponseDeserializer() {
        this(JNC.GSON);
    }
    
    public JsonResponseDeserializer(Gson gson) {
        this.gson = gson;
    }
    
    
    public final Gson getGson() {
        return gson;
    }
    
    
    @Override
    @SuppressWarnings("unchecked")
    public <R extends RpcResponse> R deserialize(String response, Class<R> responseClass) throws RpcException {
        if (response == null)
            throw new IllegalArgumentException("Response data cannot be null.");
        if (responseClass == null)
            throw new IllegalArgumentException("Response class cannot be null.");
        if (response.isEmpty())
            throw new RpcInvalidResponseException("Received response data is empty.", response);
        
        try {
            // Parse response into JSON
            JsonObject responseJson = JNH.parseJson(response);
            
            // Check for returned RPC error
            JsonElement errorElement = responseJson.get("error");
            if (errorElement != null) {
                String errorStr = errorElement.getAsString().trim();
                if (responseClass == ResponseSuccessful.class && errorStr.equalsIgnoreCase("Empty response")) {
                    return (R)new ResponseSuccessful(true); // Fix for empty response error
                }
                throw parseException(errorStr);
            }
    
            // Deserialize response
            R responseObj = getGson().fromJson(responseJson, responseClass);
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
        String msgLc = msg.toLowerCase();
        
        // Check and parse error type
        switch (msgLc) {
            case "wallet is locked":
            case "wallet locked":
                return new RpcWalletLockedException(msg);     // Wallet locked
            case "insufficient balance":
                return new RpcInvalidArgumentException(msg);  // Invalid/bad argument
            case "rpc control is disabled":
                return new RpcControlDisabledException(msg);  // RPC control disabled
            case "cancelled":
                return new RpcRequestCancelledException(msg); // Request cancelled
            case "unable to parse json":
                return new RpcInvalidRequestJsonException(    // Invalid request body
                        "The RPC server was unable to parse the JSON request.", msg);
            case "unknown command":
                return new RpcUnknownCommandException(msg);   // Unknown command
            case "invalid header: body limit exceeded":
                return new RpcInvalidRequestJsonException(    // JSON too long
                        "The request JSON exceeded the configured maximum length.", msg);
            case "unsafe rpc not allowed":
                return new RpcCommandNotAllowedException(     // RPC unsafe
                        "The specified command is unsafe and disallowed by the node.", msg);
            case "empty response":
                return new RpcInternalException(              // Empty response internal error
                        "The server returned an \"empty response\" error.", msg);
        }
        // Try parse from prefix/suffix
        if (msgLc.startsWith("bad") || msgLc.startsWith("invalid") || msgLc.endsWith("invalid")
                || msgLc.endsWith("required")) {
            return new RpcInvalidArgumentException(msg);      // Invalid/bad argument
        } else if (msgLc.contains("not found")) {
            return new RpcEntityNotFoundException(msg);       // Unknown referenced entity
        } else if (msgLc.endsWith("is disabled")) {
            return new RpcFeatureDisabledException(msg);      // Feature is disabled
        } else if (msgLc.endsWith("not allowed")) {
            return new RpcCommandNotAllowedException(msg);    // Command not allowed
        } else if (msgLc.contains("config")) {
            return new RpcConfigForbiddenException(msg);      // Config forbids request
        } else if (msgLc.contains("json")) {
            return new RpcInvalidRequestJsonException(msg);   // Disallowed/invalid JSON request
        } else if (msgLc.startsWith("internal")) {
            return new RpcInternalException(msg);             // Internal server error
        }
        // Couldn't parse, unknown exception type
        return new RpcUnrecognizedException(msg);
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
