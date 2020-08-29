/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket.topic;

import uk.oczadly.karl.jnano.websocket.NanoWebSocketClient;
import uk.oczadly.karl.jnano.websocket.WsTopic;
import uk.oczadly.karl.jnano.websocket.topic.message.TopicMessageBootstrap;

public class WsTopicBootstrap extends WsTopic<TopicMessageBootstrap> {
    
    public WsTopicBootstrap(NanoWebSocketClient client) {
        super("bootstrap", client, TopicMessageBootstrap.class);
    }
    
}
