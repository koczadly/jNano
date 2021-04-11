/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.JsonObject;
import uk.oczadly.karl.jnano.internal.JNC;

import java.time.Instant;

/**
 * Represents a response from a Nano RPC query.
 *
 * <p>This class represents an RPC response, containing all of the structured data returned by the node.</p>
 *
 * <p>Implementations of this class should specify properties as private fields, which will be automatically injected
 * with the correct value (this behaviour can be ignored by adding the {@code transient} modifier to a field). The name
 * of the property is based on the name of the field — for instance, field {@code int someValue;} will be read from the
 * {@code some_value} JSON property.</p>
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
    public final JsonObject asJson() {
        if (rawJson == null)
            throw new IllegalStateException("JSON field not initialized!");
        return rawJson;
    }
    
    /**
     * Returns the received response data as a prettified JSON string.
     * @return the response as a JSON string
     */
    @Override
    public String toString() {
        JsonObject json = rawJson;
        return json != null ? JNC.GSON_PRETTY.toJson(json) : "{}";
    }
    
    
    /**
     * Populates the raw JSON field within the {@link RpcResponse} object. This method should only be called once by a
     * deserializer instance, as this will throw an {@link IllegalStateException} on succeeding attempts.
     * @param json the raw JSON object
     */
    public final void initJsonField(JsonObject json) {
        if (this.rawJson != null)
            throw new IllegalStateException("JSON field is already initialized.");
        this.rawJson = json;
    }
    
}
