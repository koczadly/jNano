/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket.topic;

import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.websocket.NanoWebSocketClient;
import uk.oczadly.karl.jnano.websocket.Topic;

/**
 * The {@code new_unconfirmed_block} WebSocket topic.
 * <p>This topic does not support any subscription or update parameters.</p>
 * <p>Received data messages will be encoded in the {@link Block} class.</p>
 *
 * @see <a href="https://docs.nano.org/integration-guides/websockets/#new-unconfirmed-blocks">
 *     Official WebSocket documentation</a>
 */
public class TopicUnconfirmedBlocks extends Topic<Block> {
    
    public TopicUnconfirmedBlocks(NanoWebSocketClient client) {
        super("new_unconfirmed_block", Block.class, client);
    }
    
}
