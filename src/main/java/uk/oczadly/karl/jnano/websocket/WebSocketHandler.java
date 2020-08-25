package uk.oczadly.karl.jnano.websocket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.concurrent.CountDownLatch;

class WebSocketHandler extends WebSocketClient {
    
    private final NanoWebSocketClient client;
    
    public WebSocketHandler(URI serverUri, NanoWebSocketClient client) {
        super(serverUri);
        this.client = client;
    }
    
    
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        // Notify socket observer
        WsObserver observer = client.getWsObserver();
        if (observer != null) {
            client.getListenerExecutor().submit(() -> {
                observer.onOpen(handshakedata.getHttpStatus());
            });
        }
    }
    
    @Override
    public void onMessage(String message) {
        JsonObject json = JsonParser.parseString(message).getAsJsonObject();
        boolean handled = false;
        
        if (json.has("ack") && json.has("id")) {
            // Acknowledgement response
            long id = Long.parseLong(json.get("id").getAsString(), 16);
            CountDownLatch latch = client.getRequestTrackers().remove(id);
            if (latch != null && latch.getCount() > 0) {
                latch.countDown(); // Trigger
                handled = true;
            }
        } else if (json.has("message")) {
            // New message
            WsTopic<?> wsTopic = client.getTopics().get(json.get("topic").getAsString());
            
            if (wsTopic != null) {
                client.getListenerExecutor().submit(() -> {
                    wsTopic.notifyListeners(json);
                });
                handled = true;
            }
        }
        
        // Notify socket observer
        WsObserver observer = client.getWsObserver();
        if (observer != null) {
            boolean finalHandled = handled;
            client.getListenerExecutor().submit(() -> {
                observer.onMessage(json, finalHandled);
            });
        }
    }
    
    @Override
    public void onClose(int code, String reason, boolean remote) {
        // Notify waiting subscriptions
        for (CountDownLatch latch : client.getRequestTrackers().values()) {
            if (latch != null && latch.getCount() > 0) {
                latch.countDown(); // Trigger
            }
        }
        client.getRequestTrackers().clear();
    
        // Notify socket observer
        WsObserver observer = client.getWsObserver();
        if (observer != null) {
            client.getListenerExecutor().submit(() -> {
                observer.onClose(code, reason, remote);
            });
        }
    }
    
    @Override
    public void onError(Exception ex) {
        // Notify socket observer
        WsObserver observer = client.getWsObserver();
        if (observer != null) {
            client.getListenerExecutor().submit(() -> {
                observer.onError(ex);
            });
        } else {
            ex.printStackTrace();
        }
    }
    
}
