/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket;

import com.google.gson.JsonObject;
import uk.oczadly.karl.jnano.websocket.topic.message.MessageContext;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Topic<M> {
    
    protected static final String ACTION_SUBSCRIBE = "subscribe";
    protected static final String ACTION_UNSUBSCRIBE = "unsubscribe";
    
    private final String topicName;
    private final Class<M> classMessage;
    private final NanoWebSocketClient client;
    private final CopyOnWriteArrayList<TopicListener<M>> listeners = new CopyOnWriteArrayList<>();
    
    public Topic(String topicName, NanoWebSocketClient client, Class<M> classMessage) {
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
        processRequest(createJson(ACTION_SUBSCRIBE));
    }
    
    /**
     * <p>Subscribe to this topic without any options or configurations. The underlying WebSocket <em>must</em> be open
     * before you call this method.</p>
     * <p>This method will block indefinitely and wait for the associated acknowledgement message to be received before
     * continuing and returning true, false if the WebSocket closed, or throw an {@link InterruptedException} if the
     * thread is interrupted.</p>
     *
     * @return true if the action completed successfully, false if the socket is closed
     *
     * @throws IllegalStateException if the WebSocket is not currently open
     * @throws InterruptedException if the thread is interrupted
     */
    public final boolean subscribeBlocking() throws InterruptedException {
        return subscribeBlocking(0);
    }
    
    /**
     * <p>Subscribe to this topic without any options or configurations. The underlying WebSocket <em>must</em> be open
     * before you call this method.</p>
     * <p>This method will block and wait for the associated acknowledgement message to be received before
     * continuing and returning true, false if the WebSocket closed or the timeout period expires, or throw an
     *
     * {@link InterruptedException} if the thread is interrupted.</p>
     * @param timeout the timeout in milliseconds, or zero for no timeout
     * @return true if the action completed successfully, false if the socket is closed or the timeout expires
     *
     * @throws IllegalStateException if the WebSocket is not currently open
     * @throws InterruptedException if the thread is interrupted
     */
    public final boolean subscribeBlocking(long timeout) throws InterruptedException {
        return processRequest(createJson(ACTION_SUBSCRIBE), timeout);
    }
    
    
    /**
     * <p>Unsubscribes from this topic without any options or configurations. The underlying WebSocket <em>must</em> be
     * open before you call this method.</p>
     * <p>This method will process asynchronously and will not block the thread or verify completion.</p>
     *
     * @throws IllegalStateException if the WebSocket is not currently open
     */
    public final void unsubscribe() {
        processRequest(createJson(ACTION_UNSUBSCRIBE));
    }
    
    /**
     * <p>Unsubscribes from this topic without any options or configurations. The underlying WebSocket <em>must</em> be
     * open before you call this method.</p>
     * <p>This method will block indefinitely and wait for the associated acknowledgement message to be received before
     * continuing and returning true, false if the WebSocket closed, or throw an {@link InterruptedException} if the
     * thread is interrupted.</p>
     *
     * @return true if the action completed successfully, false if the socket is closed
     *
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
     *
     * @param timeout the timeout in milliseconds, or zero for no timeout
     * @return true if the action completed successfully, false if the socket is closed or the timeout expires
     *
     * @throws IllegalStateException if the WebSocket is not currently open
     * @throws InterruptedException if the thread is interrupted
     */
    public final boolean unsubscribeBlocking(long timeout) throws InterruptedException {
        return processRequest(createJson(ACTION_UNSUBSCRIBE), timeout);
    }
    
    
    /**
     * Processes an asynchronous request.
     * @param request the request object
     */
    protected final void processRequest(JsonObject request) {
        client.processRequest(request);
    }
    
    /**
     * Processes a synchronous (blocking) request.
     * @param request the request object
     * @param timeout the time out in millis, or {@code 0} for no timeout
     * @return {@code true} if the request succeeded
     * @throws InterruptedException if the thread is interrupted
     */
    protected final boolean processRequest(JsonObject request, long timeout) throws InterruptedException {
        return client.processRequestAck(request, timeout);
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
                WsObserver wsObserver = client.getObserver();
                if (wsObserver != null) {
                    wsObserver.onHandlerError(e); // Notify socket listener of exception
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
    
}
