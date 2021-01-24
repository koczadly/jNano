/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Represents a response from a Nano RPC query.
 * <p>This class represents an RPC response, containing all of the structured data returned by the node.</p>
 * <p>Classes which extend this class need to specify parameters as private fields, and MUST be marked with Gson's
 * {@link Expose} annotation. The parameter name will be derived from the name of the field, unless specified otherwise
 * using the {@link SerializedName} annotation.</p>
 */
public abstract class RpcResponse {
    
    private volatile JsonObject rawJson;
    
    
    /**
     * @return the raw JSON response data sent from the node
     */
    public final JsonObject getRawResponseJson() {
        return this.rawJson;
    }
    
    /**
     * @return the JSON-encoded response string sent from the server
     */
    @Override
    public final String toString() {
        return rawJson != null ? rawJson.toString() : "{}";
    }
    
    
    /**
     * Populates the raw JSON field within the {@link RpcResponse} object.
     * @param response the response object
     * @param json     the raw JSON object
     */
    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public static void initJsonField(RpcResponse response, JsonObject json) {
        if (response.rawJson == null) {
            synchronized (response) {
                if (response.rawJson == null) {
                    response.rawJson = json;
                    return;
                }
            }
        }
        throw new IllegalStateException("Raw JSON object is already initialized.");
    }
    
}
