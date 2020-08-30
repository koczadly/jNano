/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket.topic;

import uk.oczadly.karl.jnano.websocket.NanoWebSocketClient;
import uk.oczadly.karl.jnano.websocket.Topic;
import uk.oczadly.karl.jnano.websocket.topic.message.TopicMessageActiveDifficulty;

/**
 * The {@code active_difficulty} WebSocket topic.
 * <p>This topic does not support any subscription or update parameters.</p>
 * <p>Received data messages will be encoded in the {@link TopicMessageActiveDifficulty} class.</p>
 *
 * @see <a href="https://docs.nano.org/integration-guides/websockets/#active-difficulty">
 *     Official WebSocket documentation</a>
 */
public class TopicActiveDifficulty extends Topic<TopicMessageActiveDifficulty> {
    
    public TopicActiveDifficulty(NanoWebSocketClient client) {
        super("active_difficulty", client, TopicMessageActiveDifficulty.class);
    }
    
}
