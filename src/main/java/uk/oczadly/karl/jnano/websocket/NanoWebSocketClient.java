/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import uk.oczadly.karl.jnano.internal.JNC;
import uk.oczadly.karl.jnano.internal.utils.IDRequestTracker;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * <p>This class represents a WebSocket which can interact with the Nano WebSocket RPC API.</p>
 *
 * <p>Instances of this class may be re-used after closing through the {@link #connect()} method, without having to
 * re-create another instance. Note that calling {@link #connect()} after disconnecting from an existing connection
 * will reset all of the subscriptions and option parameters.</p>
 *
 * <p>It is recommended to use the {@link #setObserver(WsObserver)} method to listen for network and uncaught
 * exceptions, as well as receiving other miscellaneous network events WebSocket events.</p>
 *
 * <p>Below is an example of how this class should be utilised in a standard scenario:</p>
 * <pre>{@code
 * NanoWebSocketClient ws = new NanoWebSocketClient(); // Defaults to endpoint localhost:7078
 * ws.connect(); // Connect to the websocket
 *
 * // Register a listener for block confirmations
 * ws.getTopics().topicConfirmedBlocks().registerListener((message, context) -> {
 *     System.out.println("New block: " + message.getHash()); // Print the hash of all new blocks
 * });
 *
 * // Subscribe to the block confirmations topic, and specify an account filter
 * ws.getTopics().topicConfirmedBlocks().subscribe(new TopicConfirmation.SubParams()
 *         .setAccounts("nano_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz")
 *         .setIncludeBlock(true));
 * }</pre>
 */
public final class NanoWebSocketClient {
    
    private final URI uri;
    private volatile WebSocketHandler ws;
    private volatile WsObserver wsObserver = WsObserver.DEFAULT;
    private volatile IDRequestTracker<Void> requestTracker;
    
    private final Gson gson = JNC.GSON;
    private final ExecutorService listenerExecutors = Executors.newFixedThreadPool(500);
    private final TopicRegistry topicRegistry = new TopicRegistry(this);
    
    
    /**
     * Configures a WebSocket endpoint on localhost, port 7078.
     */
    public NanoWebSocketClient() {
        this(URI.create("ws://[::1]:7078"));
    }
    
    /**
     * Configures a WebSocket with the specified endpoint URI.
     * @param uri the websocket URI
     */
    public NanoWebSocketClient(URI uri) {
        this.uri = uri;
    }
    
    
    /**
     * @return the URI of the websocket
     */
    public URI getURI() {
        return uri;
    }
    
    /**
     * @return true if the websocket is currently running
     */
    public synchronized boolean isOpen() {
        return ws != null && ws.isOpen();
    }
    
    /**
     * Attempts to connect to the websocket. This method will block the thread until the connection has succeeded.
     * Note that {@link NanoWebSocketClient} instances can be re-used after closing, without having to create a new
     * object.
     * @return true if the websocket has connected
     * @throws InterruptedException if the thread is interrupted
     */
    public synchronized boolean connect() throws InterruptedException {
        if (isOpen())
            throw new IllegalStateException("WebSocket is already open.");
    
        this.requestTracker = new IDRequestTracker<>();
        this.ws = new WebSocketHandler(uri, topicRegistry, requestTracker, wsObserver, listenerExecutors);
        return ws.connectBlocking();
    }
    
    /**
     * Closes the currently-open websocket.
     */
    public synchronized void close() {
        if (!isOpen())
            throw new IllegalStateException("The WebSocket is not currently open.");
        ws.close();
    }
    
    /**
     * @return the Gson instance used for deserializing JSON messages
     */
    public Gson getGson() {
        return gson;
    }
    
    
    /**
     * Sets the observer for this WebSocket. This is not normally necessary, but can provide additional information
     * about the status and activity of this WebSocket. Only one listener may be registered for each WebSocket. This
     * method will only work while the socket is unopened or closed.
     * @param wsObserver the socket listener
     * @throws IllegalStateException if the socket is currently open
     */
    public synchronized void setObserver(WsObserver wsObserver) {
        if (isOpen())
            throw new IllegalStateException("The socket listener cannot be updated while the WebSocket is open.");
        this.wsObserver = wsObserver;
    }
    
    /**
     * @return the socket listener object
     */
    public WsObserver getObserver() {
        return wsObserver;
    }
    
    /**
     * Returns the topic registry, which contains all the available topics which can be subscribed to.
     * @return the topic registry
     */
    public TopicRegistry getTopics() {
        return topicRegistry;
    }
    
    
    /**
     * Asynchronously send the JSON object to the node.
     * @param json the json data
     */
    protected void send(JsonObject json) {
        if (!isOpen())
            throw new IllegalStateException("WebSocket is not currently open.");
        ws.send(json.toString());
    }
    
    /**
     * Send the JSON object to the node, and block until acknowledged.
     * @param json    the json data
     * @param timeout the timeout in millis
     * @return true if acknowledged
     * @throws InterruptedException if interrupted whilst waiting
     */
    protected boolean sendAck(JsonObject json, long timeout) throws InterruptedException {
        if (!isOpen())
            throw new IllegalStateException("WebSocket is not currently open.");
        
        // Send
        IDRequestTracker<Void>.Tracker tracker = requestTracker.newTracker();
        json.addProperty("id", tracker.getID());
        json.addProperty("ack", true);
        ws.send(json.toString());
        
        // Await ack
        try {
            if (timeout <= 0) {
                tracker.await();
            } else {
                tracker.await(timeout, TimeUnit.MILLISECONDS);
            }
            return true;
        } catch (IDRequestTracker.TrackerExpiredException | TimeoutException e) {
            return false;
        }
    }

}
