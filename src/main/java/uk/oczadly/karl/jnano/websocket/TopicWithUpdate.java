package uk.oczadly.karl.jnano.websocket;

public interface TopicWithUpdate<T> {
    
    /**
     * Updates the current options or filters for this topic. This method will process asynchronously and will not
     * block the thread. The underlying websocket must be open before you can call this method.
     * @param options an object with the configured options for this topic
     * @throws IllegalStateException if the websocket is not currently open
     */
    void update(T options);
    
    /**
     * Updates the current options or filters for this topic. This method will block and wait for the associated
     * acknowledgement response to be received before continuing, or throw an {@link InterruptedException} if the
     * timeout period expires. The underlying websocket must be open before you can call this method.
     * @param options an object with the configured options for this topic
     * @param timeout the timeout in milliseconds, or zero for no timeout
     * @return true if the action completed successfully, false if the timeout period expired
     * @throws IllegalStateException if the websocket is not currently open
     * @throws InterruptedException if the thread is interrupted
     */
    boolean update(T options, long timeout) throws InterruptedException;

}
