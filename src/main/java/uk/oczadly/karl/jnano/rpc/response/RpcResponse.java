/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNC;

import java.time.Instant;

/**
 * Represents a response from a Nano RPC query.
 *
 * <p>This class represents an RPC response, containing all of the structured data returned by the node.</p>
 *
 * <p>Classes which extend this class need to specify parameters as private fields, and MUST be marked with Gson's
 * {@link Expose} annotation. The parameter name will be derived from the name of the field, unless specified otherwise
 * using the {@link SerializedName} annotation.</p>
 *
 * <p>If implementing a custom deserializer, you should call the {@link #initJsonField(JsonObject)} on the response
 * object before passing it to the application.</p>
 */
public abstract class RpcResponse {
    
    private final Instant timestamp = Instant.now();
    private volatile JsonObject rawJson;
    
    
    /**
     * Returns the {@link Instant} of when this RpcResponse was received and constructed.
     * @return the timestamp of this response object
     */
    public final Instant getResponseTimestamp() {
        return timestamp;
    }
    
    /**
     * Returns this response object as a {@link JsonObject}. This contains the raw data, and will include any additional
     * values which aren't made accessible by the getter methods.
     * @return the raw JSON response data sent from the node
     */
    public synchronized final JsonObject asJson() {
        if (rawJson == null)
            throw new IllegalStateException("JSON field not initialized!");
        return rawJson;
    }
    
    /**
     * Returns the received response data as a prettified JSON string.
     * @return the response as a JSON string
     */
    @Override
    public synchronized final String toString() {
        return rawJson != null ? JNC.GSON_PRETTY.toJson(rawJson) : "{}";
    }
    
    
    /**
     * Populates the raw JSON field within the {@link RpcResponse} object. This method should only be called once by a
     * deserializer instance, as this will throw an {@link IllegalStateException} on succeeding attempts.
     * @param json the raw JSON object
     */
    public synchronized void initJsonField(JsonObject json) {
        if (this.rawJson != null)
            throw new IllegalStateException("JSON field is already initialized.");
        this.rawJson = json;
    }
    
}
