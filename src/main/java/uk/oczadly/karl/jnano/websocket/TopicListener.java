/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket;

import uk.oczadly.karl.jnano.websocket.topic.message.MessageContext;

public interface TopicListener<T> {
    
    /**
     * This method is called when a new message for the topic is received.
     * @param message the message contents
     * @param context additional context about the message
     */
    void onMessage(T message, MessageContext context);
    
}
