/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket.topic;

import uk.oczadly.karl.jnano.websocket.NanoWebSocketClient;
import uk.oczadly.karl.jnano.websocket.Topic;
import uk.oczadly.karl.jnano.websocket.topic.message.TopicMessageWork;

/**
 * The {@code work} WebSocket topic.
 * <p>This topic does not support any subscription or update parameters.</p>
 * <p>Received data messages will be encoded in the {@link TopicMessageWork} class.</p>
 *
 * @see <a href="https://docs.nano.org/integration-guides/websockets/#proof-of-work">
 *     Official WebSocket documentation</a>
 */
public class TopicWork extends Topic<TopicMessageWork> {
    
    public TopicWork(NanoWebSocketClient client) {
        super("work", TopicMessageWork.class, client);
    }
    
}
