package uk.oczadly.karl.jnano.websocket.topic;

import uk.oczadly.karl.jnano.websocket.NanoWebSocketClient;
import uk.oczadly.karl.jnano.websocket.WsTopic;
import uk.oczadly.karl.jnano.websocket.topic.message.TopicMessageTelemetry;

public class WsTopicTelemetry extends WsTopic<TopicMessageTelemetry> {
    
    public WsTopicTelemetry(NanoWebSocketClient client) {
        super("telemetry", client, TopicMessageTelemetry.class);
    }
    
}
