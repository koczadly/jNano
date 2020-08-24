package uk.oczadly.karl.jnano.websocket.topic;

import uk.oczadly.karl.jnano.websocket.NanoWebSocketClient;
import uk.oczadly.karl.jnano.websocket.WsTopic;
import uk.oczadly.karl.jnano.websocket.topic.message.TopicMessageActiveDifficulty;

public class WsTopicActiveDifficulty extends WsTopic<TopicMessageActiveDifficulty> {
    
    public WsTopicActiveDifficulty(NanoWebSocketClient client) {
        super("active_difficulty", client, TopicMessageActiveDifficulty.class);
    }
    
}
