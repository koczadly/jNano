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

/**
 * The standard implementation of {@link RpcResponseDeserializer}, which deserializes the response as a JSON object.
 */
public class JsonResponseDeserializer implements RpcResponseDeserializer {
    
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
    public final <R extends RpcResponse> R deserialize(String response, Class<R> responseClass) throws RpcException {
        if (response == null) throw new IllegalArgumentException("Response data cannot be null.");
        if (responseClass == null) throw new IllegalArgumentException("Response class cannot be null.");
        if (response.isEmpty()) throw new RpcInvalidResponseException("Received response data is empty.", response);
        
        try {
            // Parse response into JSON
            JsonObject jsonResponse = parseJson(response);
            
            // Check for returned RPC error
            JsonElement error = jsonResponse.get("error");
            if (error != null) {
                String errorStr = error.getAsString().trim();
                // Fix for empty response error
                if (responseClass == ResponseSuccessful.class && errorStr.equalsIgnoreCase("Empty response")) {
                    return (R)new ResponseSuccessful(true);
                }
                // Parse and throw exception
                RpcExternalException exception = parseException(errorStr);
                throw exception != null ? exception : new RpcUnrecognizedException(errorStr);
            }
            
            // Deserialize and return
            return deserialize(jsonResponse, responseClass);
        } catch (JsonParseException ex) {
            throw new RpcInvalidResponseException(ex, response); // If unable to parse
        }
    }
    
    
    /**
     * Parses the response string to a {@link JsonObject}, and performs any additional non-JSON exception parsing.
     *
     * <p>If an {@link JsonParseException} is thrown by this method, then an {@link RpcInvalidResponseException} will
     * be automatically generated and thrown.</p>
     *
     * @param response the response data
     * @return the response data, as a JsonObject
     * @throws RpcException if some other unexpected error occurs
     */
    protected JsonObject parseJson(String response) throws RpcException {
        return JNH.parseJson(response);
    }
    
    /**
     * Deserializes the response to the specified response class. Any returned errors should already have been parsed
     * by {@link #parseException(String)} when this method is called.
     *
     * @param json          the response json
     * @param responseClass the response class
     * @param <R>           the response class type
     * @return the deserialized value
     * @throws RpcException if some other unexpected error occurs
     */
    protected <R extends RpcResponse> R deserialize(JsonObject json, Class<R> responseClass) throws RpcException {
        R responseObj = gson.fromJson(json, responseClass);
        responseObj.initJsonField(json);
        return responseObj;
    }
    
    /**
     * Parses the received "{@code error}" message into the appropriate {@link RpcExternalException} subclass.
     * @param rawMessage the received raw error message
     * @return the parsed exception object to be thrown, or null if the exception type could not be identified
     */
    protected RpcExternalException parseException(String rawMessage) {
        return parseErrorMessage(rawMessage);
    }
    
    
    /**
     * Parses an {@link RpcExternalException} from a given response message.
     * @param rawMessage the received raw error message
     * @return the parsed exception object to be thrown
     */
    public static RpcExternalException parseErrorMessage(String rawMessage) {
        String msgLc = rawMessage.toLowerCase();
        
        // Check and parse error type
        switch (msgLc) {
            case "wallet is locked":
            case "wallet locked":
                return new RpcWalletLockedException(rawMessage);     // Wallet locked
            case "insufficient balance":
                return new RpcInvalidArgumentException(rawMessage);  // Invalid/bad argument
            case "rpc control is disabled":
                return new RpcControlDisabledException(rawMessage);  // RPC control disabled
            case "cancelled":
                return new RpcRequestCancelledException(rawMessage); // Request cancelled
            case "unable to parse json":
                return new RpcInvalidRequestJsonException(    // Invalid request body
                        "The RPC server was unable to parse the JSON request.", rawMessage);
            case "unknown command":
                return new RpcUnknownCommandException(rawMessage);   // Unknown command
            case "invalid header: body limit exceeded":
                return new RpcInvalidRequestJsonException(    // JSON too long
                        "The request JSON exceeded the configured maximum length.", rawMessage);
            case "unsafe rpc not allowed":
                return new RpcCommandNotAllowedException(     // RPC unsafe
                        "The specified command is unsafe and disallowed by the node.", rawMessage);
            case "empty response":
                return new RpcNodeInternalErrorException(              // Empty response internal error
                        "The server returned an \"empty response\" error.", rawMessage);
        }
        // Try parse from prefix/suffix
        if (msgLc.startsWith("bad ") || msgLc.startsWith("invalid ") || msgLc.startsWith("gap ")
                || msgLc.endsWith(" invalid") || msgLc.endsWith(" required") || msgLc.endsWith(" do not match")) {
            return new RpcInvalidArgumentException(rawMessage);      // Invalid/bad argument
        } else if (msgLc.contains("not found")) {
            return new RpcEntityNotFoundException(rawMessage);       // Unknown referenced entity
        } else if (msgLc.endsWith("is disabled")) {
            return new RpcFeatureDisabledException(rawMessage);      // Feature is disabled
        } else if (msgLc.endsWith("not allowed")) {
            return new RpcCommandNotAllowedException(rawMessage);    // Command not allowed
        } else if (msgLc.contains("config")) {
            return new RpcConfigForbiddenException(rawMessage);      // Config forbids request
        } else if (msgLc.contains("json") || msgLc.contains("malformed")) {
            return new RpcInvalidRequestJsonException(rawMessage);   // Disallowed/invalid JSON request
        } else if (msgLc.startsWith("internal")) {
            return new RpcNodeInternalErrorException(rawMessage);             // Internal server error
        }
        // Couldn't parse, unknown exception type
        return new RpcUnrecognizedException(rawMessage);
    }

}
