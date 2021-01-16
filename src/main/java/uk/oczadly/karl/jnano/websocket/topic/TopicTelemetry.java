/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket.topic;

import uk.oczadly.karl.jnano.websocket.NanoWebSocketClient;
import uk.oczadly.karl.jnano.websocket.Topic;
import uk.oczadly.karl.jnano.websocket.topic.message.TopicMessageTelemetry;

/**
 * The {@code telemetry} WebSocket topic.
 * <p>This topic does not support any subscription or update parameters.</p>
 * <p>Received data messages will be encoded in the {@link TopicMessageTelemetry} class.</p>
 *
 * @see <a href="https://docs.nano.org/integration-guides/websockets/#node-telemetry">
 *     Official WebSocket documentation</a>
 */
public class TopicTelemetry extends Topic<TopicMessageTelemetry> {
    
    public TopicTelemetry(NanoWebSocketClient client) {
        super("telemetry", TopicMessageTelemetry.class, client);
    }
    
}
