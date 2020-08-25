package uk.oczadly.karl.jnano.websocket;

public interface TopicWithOptions<T> {
    
    /**
     * <p>Subscribe to this topic with a set of defined options. The underlying WebSocket <em>must</em> be open before
     * you call this method.</p>
     * <p>This method will process asynchronously and will not block the thread or verify completion.</p>
     * @param options an object with the configured options for this topic
     * @throws IllegalStateException if the WebSocket is not currently open
     */
    void subscribe(T options);
    
    /**
     * <p>Subscribes to this topic with a set of defined options. The underlying WebSocket <em>must</em> be open before
     * you call this method.</p>
     * <p>This method will block indefinitely and wait for the associated acknowledgement message to be received before
     * continuing and returning true, false if the WebSocket closed, or throw an {@link InterruptedException} if the
     * thread is interrupted.</p>
     * @param options an object with the configured options for this topic
     * @return true if the action completed successfully, false if the WS is closed
     * @throws IllegalStateException if the WebSocket is not currently open
     * @throws InterruptedException if the thread is interrupted
     */
    default boolean subscribeBlocking(T options) throws InterruptedException {
        return subscribeBlocking(0, options);
    }
    
    /**
     * <p>Subscribes to this topic with a set of defined options. The underlying WebSocket <em>must</em> be open before
     * you call this method.</p>
     * <p>This method will block and wait for the associated acknowledgement message to be received before
     * continuing and returning true, false if the WebSocket closed or the timeout period expires, or throw an
     * {@link InterruptedException} if the thread is interrupted.</p>
     * @param timeout the timeout in milliseconds, or zero for no timeout
     * @param options an object with the configured options for this topic
     * @return true if the action completed successfully, false if the WS is closed or the timeout expires
     * @throws IllegalStateException if the WebSocket is not currently open
     * @throws InterruptedException if the thread is interrupted
     */
    boolean subscribeBlocking(long timeout, T options) throws InterruptedException;

}
