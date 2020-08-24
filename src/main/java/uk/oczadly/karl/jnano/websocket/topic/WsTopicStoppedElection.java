package uk.oczadly.karl.jnano.websocket.topic;

import uk.oczadly.karl.jnano.websocket.NanoWebSocketClient;
import uk.oczadly.karl.jnano.websocket.WsTopic;
import uk.oczadly.karl.jnano.websocket.topic.message.TopicMessageStoppedElection;

public class WsTopicStoppedElection extends WsTopic<TopicMessageStoppedElection> {
    
    public WsTopicStoppedElection(NanoWebSocketClient client) {
        super("stopped_election", client, TopicMessageStoppedElection.class);
    }
    
}
