/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket.topic.message;

import com.google.gson.JsonObject;
import uk.oczadly.karl.jnano.websocket.NanoWebSocketClient;

import java.time.Instant;

public class MessageContext {
    
    private final NanoWebSocketClient wsClient;
    private final Instant timestamp;
    private final JsonObject json;
    
    public MessageContext(NanoWebSocketClient wsClient, Instant timestamp, JsonObject json) {
        this.wsClient = wsClient;
        this.timestamp = timestamp;
        this.json = json;
    }
    
    
    public NanoWebSocketClient getClient() {
        return wsClient;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }
    
    public JsonObject getJsonObject() {
        return json;
    }
    
}
