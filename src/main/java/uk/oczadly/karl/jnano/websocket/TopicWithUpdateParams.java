/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket;

public class TopicWithUpdateParams<M, S, U> extends TopicWithSubParams<M, S> {
    
    protected static final String ACTION_UPDATE = "update";
    
    public TopicWithUpdateParams(String topicName, Class<M> classMessage, NanoWebSocketClient client) {
        super(topicName, classMessage, client);
    }
    
    
    /**
     * <p>Subscribe to this topic with a set of defined parameters. The underlying WebSocket <em>must</em> be open
     * before you call this method.</p>
     * <p>This method will process asynchronously and will not block the thread or verify completion.</p>
     *
     * @param args an object containing the configuration arguments for this topic
     *
     * @throws IllegalStateException if the WebSocket is not currently open
     */
    public final void update(U args) {
        processRequest(createJson(ACTION_UPDATE, args));
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
    public final boolean updateBlocking(U args) throws InterruptedException {
        return updateBlocking(0, args);
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
    public final boolean updateBlocking(long timeout, U args) throws InterruptedException {
        return processRequest(createJson(ACTION_UPDATE, args), timeout);
    }
    
}
