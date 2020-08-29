/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket.topic;

import uk.oczadly.karl.jnano.websocket.NanoWebSocketClient;
import uk.oczadly.karl.jnano.websocket.TopicWithOptions;
import uk.oczadly.karl.jnano.websocket.TopicWithUpdate;
import uk.oczadly.karl.jnano.websocket.WsTopic;
import uk.oczadly.karl.jnano.websocket.topic.message.TopicMessageConfirmation;
import uk.oczadly.karl.jnano.websocket.topic.options.TopicOptionsConfirmation;
import uk.oczadly.karl.jnano.websocket.topic.options.TopicUpdateConfirmation;

public class WsTopicConfirmation extends WsTopic<TopicMessageConfirmation>
        implements TopicWithOptions<TopicOptionsConfirmation>, TopicWithUpdate<TopicUpdateConfirmation> {
    
    public WsTopicConfirmation(NanoWebSocketClient client) {
        super("confirmation", client, TopicMessageConfirmation.class);
    }
    
    @Override
    public void subscribe(TopicOptionsConfirmation options) {
        _subscribe(options);
    }
    
    @Override
    public boolean subscribeBlocking(long timeout, TopicOptionsConfirmation options) throws InterruptedException {
        return _subscribeBlocking(timeout, options);
    }
    
    @Override
    public void update(TopicUpdateConfirmation options) {
        _update(options);
    }
    
    @Override
    public boolean updateBlocking(long timeout, TopicUpdateConfirmation options) throws InterruptedException {
        return _updateBlocking(timeout, options);
    }
    
}
