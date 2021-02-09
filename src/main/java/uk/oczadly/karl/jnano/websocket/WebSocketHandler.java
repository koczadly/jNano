/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket;

import com.google.gson.JsonObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.internal.utils.IDRequestTracker;

import java.net.URI;
import java.util.concurrent.ExecutorService;

class WebSocketHandler extends WebSocketClient {
    
    private final TopicRegistry topicRegistry;
    private final IDRequestTracker<Void> requestTracker;
    private final WsObserver observer;
    private final ExecutorService listenerExecutor;
    
    public WebSocketHandler(URI serverUri, TopicRegistry topicRegistry, IDRequestTracker<Void> requestTracker,
                            WsObserver observer, ExecutorService listenerExecutor) {
        super(serverUri);
        this.topicRegistry = topicRegistry;
        this.requestTracker = requestTracker;
        this.observer = observer;
        this.listenerExecutor = listenerExecutor;
    }
    
    
    @Override
    public void onOpen(ServerHandshake handshake) {
        // Notify socket observer
        if (observer != null)
            listenerExecutor.submit(() -> observer.onOpen(handshake.getHttpStatus()));
    }
    
    @Override
    public void onMessage(String message) {
        try {
            JsonObject json = JNH.parseJson(message);
            boolean handled = false;
    
            if (json.has("ack") && json.has("id")) {
                // Acknowledgement response (notify trackers)
                requestTracker.complete(json.get("id").getAsString(), null);
                handled = true;
            } else if (json.has("message")) {
                // New message
                Topic<?> wsTopic = topicRegistry.get(json.get("topic").getAsString());
                if (wsTopic != null) {
                    listenerExecutor.submit(() -> wsTopic.notifyListeners(json));
                    handled = true;
                }
            }
    
            // Notify socket observer
            if (observer != null) {
                boolean finalHandled = handled;
                listenerExecutor.submit(() -> observer.onMessage(json, finalHandled));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onClose(int code, String reason, boolean remote) {
        // Notify waiting trackers
        requestTracker.cancelAll();
    
        // Notify socket observer
        if (observer != null)
            listenerExecutor.submit(() -> observer.onClose(code, reason, remote));
    }
    
    @Override
    public void onError(Exception ex) {
        // Notify socket observer
        if (observer != null)
            listenerExecutor.submit(() -> observer.onSocketError(ex));
    }
    
}
