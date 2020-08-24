package uk.oczadly.karl.jnano.websocket.topic;

import uk.oczadly.karl.jnano.websocket.NanoWebSocketClient;
import uk.oczadly.karl.jnano.websocket.WsTopic;
import uk.oczadly.karl.jnano.websocket.topic.message.TopicMessageWork;

public class WsTopicWork extends WsTopic<TopicMessageWork> {
    
    public WsTopicWork(NanoWebSocketClient client) {
        super("work", client, TopicMessageWork.class);
    }
    
}