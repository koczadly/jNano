package uk.oczadly.karl.jnano.websocket;

import com.google.gson.JsonObject;
import uk.oczadly.karl.jnano.websocket.topic.message.MessageContext;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WsTopic<M> {
    
    private final String topicName;
    private final Class<M> classMessage;
    private final NanoWebSocketClient client;
    private final CopyOnWriteArrayList<TopicListener<M>> listeners = new CopyOnWriteArrayList<>();
    
    public WsTopic(String topicName, NanoWebSocketClient client, Class<M> classMessage) {
        this.topicName = topicName;
        this.client = client;
        this.classMessage = classMessage;
    }
    
    
    /**
     * @return the name of the topic
     */
    public final String getTopicName() {
        return topicName;
    }
    
    /**
     * @return the class type which is used to represent messages for this topic
     */
    public final Class<M> getMessageClass() {
        return classMessage;
    }
    
    /**
     * @return the client which handles this topic
     */
    protected final NanoWebSocketClient getClient() {
        return client;
    }
    
    /**
     * Registers a new listener for this topic.
     * @param listener the listener to register
     */
    public final void registerListener(TopicListener<M> listener) {
        listeners.add(listener);
    }
    
    /**
     * De-registers a listener for this topic.
     * @param listener the listener to remove
     * @return true if the listener was removed
     */
    public final boolean deregisterListener(TopicListener<M> listener) {
        return listeners.remove(listener);
    }
    
    /**
     * @return a list of objects listening to this topic
     */
    public final List<TopicListener<M>> getListeners() {
        return Collections.unmodifiableList(listeners);
    }
    
    
    /**
     * <p>Subscribe to this topic without any options or configurations. The underlying WebSocket <em>must</em> be open
     * before you call this method.</p>
     * <p>This method will process asynchronously and will not block the thread or verify completion.</p>
     * @throws IllegalStateException if the WebSocket is not currently open
     */
    public final void subscribe() {
        _subscribe(null);
    }
    
    /**
     * <p>Subscribe to this topic without any options or configurations. The underlying WebSocket <em>must</em> be open
     * before you call this method.</p>
     * <p>This method will block indefinitely and wait for the associated acknowledgement message to be received before
     * continuing and returning true, false if the WebSocket closed, or throw an {@link InterruptedException} if the
     * thread is interrupted.</p>
     * @return true if the action completed successfully, false if the WS is closed
     * @throws IllegalStateException if the WebSocket is not currently open
     * @throws InterruptedException if the thread is interrupted
     */
    public final boolean subscribeBlocking() throws InterruptedException {
        return subscribeBlocking(0);
    }
    
    /**
     * <p>Subscribe to this topic without any options or configurations. The underlying WebSocket <em>must</em> be open
     * before you call this method.</p>
     * <p>This method will block indefinitely and wait for the associated acknowledgement message to be received before
     * continuing and returning true, false if the WebSocket closed, or throw an {@link InterruptedException} if the
     * thread is interrupted.</p>
     * @return true if the action completed successfully, false if the WS is closed
     * @throws IllegalStateException if the WebSocket is not currently open
     * @throws InterruptedException if the thread is interrupted
     */
    public final boolean subscribeBlocking(long timeout) throws InterruptedException {
        return _subscribeBlocking(timeout, null);
    }
    
    /**
     * <p>Unsubscribes from this topic without any options or configurations. The underlying WebSocket <em>must</em> be
     * open before you call this method.</p>
     * <p>This method will process asynchronously and will not block the thread or verify completion.</p>
     * @throws IllegalStateException if the WebSocket is not currently open
     */
    public final void unsubscribe() {
        client.processRequest(createJson("unsubscribe"));
    }
    
    /**
     * <p>Unsubscribes from this topic without any options or configurations. The underlying WebSocket <em>must</em> be
     * open before you call this method.</p>
     * <p>This method will block indefinitely and wait for the associated acknowledgement message to be received before
     * continuing and returning true, false if the WebSocket closed, or throw an {@link InterruptedException} if the
     * thread is interrupted.</p>
     * @return true if the action completed successfully, false if the WS is closed
     * @throws IllegalStateException if the WebSocket is not currently open
     * @throws InterruptedException if the thread is interrupted
     */
    public final boolean unsubscribeBlocking() throws InterruptedException {
        return unsubscribeBlocking(0);
    }
    
    /**
     * <p>Unsubscribes from this topic without any options or configurations. The underlying WebSocket <em>must</em> be
     * open before you call this method.</p>
     * <p>This method will block and wait for the associated acknowledgement message to be received before
     * continuing and returning true, false if the WebSocket closed or the timeout period expires, or throw an
     * {@link InterruptedException} if the thread is interrupted.</p>
     * @param timeout the timeout in milliseconds, or zero for no timeout
     * @return true if the action completed successfully, false if the WS is closed or the timeout expires
     * @throws IllegalStateException if the WebSocket is not currently open
     * @throws InterruptedException if the thread is interrupted
     */
    public final boolean unsubscribeBlocking(long timeout) throws InterruptedException {
        return client.processRequestAck(createJson("unsubscribe"), timeout);
    }
    
    
    protected final void _subscribe(Object options) {
        client.processRequest(createJson("subscribe", options));
    }
    
    protected final boolean _subscribeBlocking(long timeout, Object options) throws InterruptedException {
        return client.processRequestAck(createJson("subscribe", options), timeout);
    }
    
    protected final void _update(Object options) {
        client.processRequest(createJson("update", options));
    }
    
    protected final boolean _updateBlocking(long timeout, Object options) throws InterruptedException {
        return client.processRequestAck(createJson("update", options), timeout);
    }
    
    
    void notifyListeners(JsonObject json) {
        if (listeners.isEmpty()) return; // Skip if no listeners
    
        // Parse
        JsonObject messageJson = json.getAsJsonObject("message");
        MessageContext context = new MessageContext(client, Instant.ofEpochMilli(json.get("time").getAsLong()),
                messageJson);
        M message = client.getGson().fromJson(messageJson, classMessage);
        
        // Notify
        for (TopicListener<M> listener : listeners) {
            try {
                listener.onMessage(message, context);
            } catch (Exception e) {
                WsObserver wsObserver = client.getWsObserver();
                if (wsObserver != null) {
                    wsObserver.onError(e); // Notify socket listener of exception
                }
            }
        }
    }
    
    
    /**
     * Creates a template {@link JsonObject} for building requests.
     * @param action  the action of the request
     * @return the json object
     */
    protected final JsonObject createJson(String action) {
        JsonObject json = new JsonObject();
        json.addProperty("action", action);
        json.addProperty("topic", topicName);
        return json;
    }
    
    /**
     * Creates a template {@link JsonObject} for building requests.
     * @param action  the action of the request
     * @param options an object containing a set of serializable options
     * @return the json object
     */
    protected final JsonObject createJson(String action, Object options) {
        JsonObject json = createJson(action);
        if (options != null)
            json.add("options", getClient().getGson().toJsonTree(options));
        return json;
    }

}
