/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket.topic;

import uk.oczadly.karl.jnano.websocket.NanoWebSocketClient;
import uk.oczadly.karl.jnano.websocket.WsTopic;
import uk.oczadly.karl.jnano.websocket.topic.message.TopicMessageUnconfirmedBlock;

public class WsTopicUnconfirmedBlocks extends WsTopic<TopicMessageUnconfirmedBlock> {
    
    public WsTopicUnconfirmedBlocks(NanoWebSocketClient client) {
        super("new_unconfirmed_block", client, TopicMessageUnconfirmedBlock.class);
    }
    
}
