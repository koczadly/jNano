/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket;

import com.google.gson.JsonObject;

public class TopicWithSubParams<M, S> extends Topic<M> {
    
    public TopicWithSubParams(String topicName, NanoWebSocketClient client, Class<M> classMessage) {
        super(topicName, client, classMessage);
    }
    
    
    /**
     * <p>Subscribe to this topic with a set of defined parameters. The underlying WebSocket <em>must</em> be open
     * before you call this method.</p>
     * <p>This method will process asynchronously and will not block the thread or verify completion.</p>
     * @param args an object containing the configuration arguments for this topic
     * @throws IllegalStateException if the WebSocket is not currently open
     */
    public final void subscribe(S args) {
        processRequest(createJson(ACTION_SUBSCRIBE, args));
    }
    
    /**
     * <p>Subscribes to this topic with a set of defined parameters. The underlying WebSocket <em>must</em> be open
     * before you call this method.</p>
     * <p>This method will block indefinitely and wait for the associated acknowledgement message to be received before
     * continuing and returning true, false if the WebSocket closed, or throw an {@link InterruptedException} if the
     * thread is interrupted.</p>
     *
     * @param args an object containing the configuration arguments for this topic
     * @return true if the action completed successfully, false if the socket is closed
     *
     * @throws IllegalStateException if the WebSocket is not currently open
     * @throws InterruptedException if the thread is interrupted
     */
    public final boolean subscribeBlocking(S args) throws InterruptedException {
        return subscribeBlocking(0, args);
    }
    
    /**
     * <p>Subscribes to this topic with a set of defined parameters. The underlying WebSocket <em>must</em> be open
     * before you call this method.</p>
     * <p>This method will block and wait for the associated acknowledgement message to be received before
     * continuing and returning true, false if the WebSocket closed or the timeout period expires, or throw an
     * {@link InterruptedException} if the thread is interrupted.</p>
     *
     * @param timeout the timeout in milliseconds, or zero for no timeout
     * @param args    an object containing the configuration arguments for this topic
     * @return true if the action completed successfully, false if the socket is closed or the timeout expires
     *
     * @throws IllegalStateException if the WebSocket is not currently open
     * @throws InterruptedException if the thread is interrupted
     */
    public final boolean subscribeBlocking(long timeout, S args) throws InterruptedException {
        return processRequest(createJson(ACTION_SUBSCRIBE, args), timeout);
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
