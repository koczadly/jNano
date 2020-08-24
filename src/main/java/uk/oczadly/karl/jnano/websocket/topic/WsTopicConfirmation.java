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
    public boolean subscribe(TopicOptionsConfirmation options, long timeout) throws InterruptedException {
        return _subscribe(options, timeout);
    }
    
    @Override
    public void update(TopicUpdateConfirmation options) {
        _update(options);
    }
    
    @Override
    public boolean update(TopicUpdateConfirmation options, long timeout) throws InterruptedException {
        return _update(options, timeout);
    }
    
}
