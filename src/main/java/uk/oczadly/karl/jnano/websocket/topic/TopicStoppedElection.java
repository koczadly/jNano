/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket.topic;

import uk.oczadly.karl.jnano.websocket.NanoWebSocketClient;
import uk.oczadly.karl.jnano.websocket.Topic;
import uk.oczadly.karl.jnano.websocket.topic.message.TopicMessageStoppedElection;

/**
 * The {@code stopped_election} WebSocket topic.
 * <p>This topic does not support any subscription or update parameters.</p>
 * <p>Received data messages will be encoded in the {@link TopicMessageStoppedElection} class.</p>
 *
 * @see <a href="https://docs.nano.org/integration-guides/websockets/#stopped-elections">
 *     Official WebSocket documentation</a>
 */
public class TopicStoppedElection extends Topic<TopicMessageStoppedElection> {
    
    public TopicStoppedElection(NanoWebSocketClient client) {
        super("stopped_election", TopicMessageStoppedElection.class, client);
    }
    
}
