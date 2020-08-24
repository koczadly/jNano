package uk.oczadly.karl.jnano.websocket.topic;

import uk.oczadly.karl.jnano.websocket.NanoWebSocketClient;
import uk.oczadly.karl.jnano.websocket.TopicWithOptions;
import uk.oczadly.karl.jnano.websocket.WsTopic;
import uk.oczadly.karl.jnano.websocket.topic.message.TopicMessageVote;
import uk.oczadly.karl.jnano.websocket.topic.options.TopicOptionsVote;

public class WsTopicVote extends WsTopic<TopicMessageVote>
        implements TopicWithOptions<TopicOptionsVote> {
    
    public WsTopicVote(NanoWebSocketClient client) {
        super("vote", client, TopicMessageVote.class);
    }
    
    
    @Override
    public void subscribe(TopicOptionsVote options) {
        _subscribe(options);
    }
    
    @Override
    public boolean subscribe(TopicOptionsVote options, long timeout) throws InterruptedException {
        return _subscribe(options, timeout);
    }
    
}
