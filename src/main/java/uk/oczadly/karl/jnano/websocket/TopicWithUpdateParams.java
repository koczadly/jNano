/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket;

public interface TopicWithUpdateParams<S, U> extends TopicWithSubscribeParams<S> {
    
    /**
     * <p>Updates the current parameters or filters for this topic. The underlying WebSocket <em>must</em> be open
     * before you call this method.</p>
     * <p>This method will process asynchronously and will not block the thread or verify completion.</p>
     * @param params an object with the configured params for this topic
     * @throws IllegalStateException if the WebSocket is not currently open
     */
    void update(U params);
    
    /**
     * <p>Updates the current parameters or filters for this topic. The underlying WebSocket <em>must</em> be open
     * before you call this method.</p>
     * <p>This method will block indefinitely and wait for the associated acknowledgement message to be received before
     * continuing and returning true, false if the WebSocket closed, or throw an {@link InterruptedException} if the
     * thread is interrupted.</p>
     * @param params an object with the configured params for this topic
     * @return true if the action completed successfully, false if the WS is closed
     * @throws IllegalStateException if the WebSocket is not currently open
     * @throws InterruptedException if the thread is interrupted
     */
    default boolean updateBlocking(U params) throws InterruptedException {
        return updateBlocking(0, params);
    }
    
    /**
     * <p>Updates the current parameters or filters for this topic. The underlying WebSocket <em>must</em> be open
     * before you call this method.</p>
     * <p>This method will block and wait for the associated acknowledgement message to be received before
     * continuing and returning true, false if the WebSocket closed or the timeout period expires, or throw an
     * {@link InterruptedException} if the thread is interrupted.</p>
     * @param timeout the timeout in milliseconds, or zero for no timeout
     * @param params an object with the configured params for this topic
     * @return true if the action completed successfully, false if the WS is closed or the timeout expires
     * @throws IllegalStateException if the WebSocket is not currently open
     * @throws InterruptedException if the thread is interrupted
     */
    boolean updateBlocking(long timeout, U params) throws InterruptedException;
    
}
