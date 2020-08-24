package uk.oczadly.karl.jnano.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.websocket.topic.*;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>This class represents a WebSocket which can interact with the Nano WebSocket RPC API.</p>
 * <p>Instances of this class may be re-used after closing through the {@link #connect()} method, without having to
 * re-create another instance. It is recommended to use the {@link #setSocketListener(SocketListener)} method to
 * listen for uncaught exceptions and WebSocket events.</p>
 * <p>Below is an example of how this class should be utilised in a standard scenario:</p>
 * <pre>{@code
 * NanoWebSocketClient ws = new NanoWebSocketClient(); // Defaults to endpoint localhost:7078
 * ws.connect(); // Connect to the websocket
 *
 * // Register a listener for block confirmations
 * ws.getTopicConfirmedBlocks().registerListener((message, context) -> {
 *     System.out.println("New block: " + message.getHash()); // Print the hash of all new blocks
 * });
 *
 * // Subscribe to the block confirmations topic, and specify an account filter
 * ws.getTopicConfirmedBlocks().subscribe(new TopicOptionsConfirmation()
 *         .setAccounts(List.of(NanoAccount.parse("nano_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz")))
 *         .setIncludeBlock(true));
 * }</pre>
 */
public final class NanoWebSocketClient {
    
    private final URI uri;
    private volatile WebSocketHandler ws;
    private volatile SocketListener socketListener;
    
    private final AtomicLong nextReqId = new AtomicLong(0);
    private final Map<Long, CountDownLatch> requestTrackers = new ConcurrentHashMap<>();
    private final Gson gson = JNH.GSON;
    private final Map<String, WsTopic<?>> topics = new ConcurrentHashMap<>();
    
    private final WsTopicConfirmation topicConfirmation = new WsTopicConfirmation(this);
    private final WsTopicVote topicVote = new WsTopicVote(this);
    private final WsTopicStoppedElection topicStoppedElection = new WsTopicStoppedElection(this);
    private final WsTopicActiveDifficulty topicActiveDifficulty = new WsTopicActiveDifficulty(this);
    private final WsTopicWork topicWork = new WsTopicWork(this);
    private final WsTopicTelemetry topicTelemetry = new WsTopicTelemetry(this);
    private final WsTopicUnconfirmedBlocks topicUnconfirmed = new WsTopicUnconfirmedBlocks(this);
    private final WsTopicBootstrap topicBootstrap = new WsTopicBootstrap(this);
    
    
    /**
     * Configures a WebSocket endpoint on localhost, port 7078.
     */
    public NanoWebSocketClient() {
        this(JNH.unchecked(() -> new URI("ws://[::1]:7078")));
    }
    
    /**
     * Configures a WebSocket with the specified endpoint URI.
     * @param uri the websocket URI
     */
    public NanoWebSocketClient(URI uri) {
        this.uri = uri;
        
        setSocketListener(SocketListener.DEFAULT);
        
        registerTopic(topicConfirmation);
        registerTopic(topicVote);
        registerTopic(topicStoppedElection);
        registerTopic(topicActiveDifficulty);
        registerTopic(topicWork);
        registerTopic(topicTelemetry);
        registerTopic(topicUnconfirmed);
        registerTopic(topicBootstrap);
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
    public boolean isOpen() {
        return ws != null && ws.isOpen();
    }
    
    /**
     * Attempts to connect to the websocket. This method will block the thread until the connection has succeeded.
     * Note that {@link NanoWebSocketClient} instances can be re-used after closing, without having to create a new
     * object.
     * @return true if the websocket has connected
     * @throws InterruptedException if the thread is interrupted
     */
    public boolean connect() throws InterruptedException {
        if (isOpen())
            throw new IllegalStateException("WebSocket is already open.");
        
        this.ws = new WebSocketHandler(uri, this);
        return ws.connectBlocking();
    }
    
    /**
     * Closes the currently-open websocket.
     */
    public void close() {
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
     * Sets the listener for this socket. This is not normally necessary, but can provide additional information
     * about the status and activity of this WebSocket. Only one listener may be registered for each WebSocket. This
     * method will only work while the socket is unopened or closed.
     * @param socketListener the socket listener
     * @throws IllegalStateException if the socket is currently open
     */
    public void setSocketListener(SocketListener socketListener) {
        if (isOpen())
            throw new IllegalStateException("The socket listener cannot be updated while the WebSocket is open.");
        
        this.socketListener = socketListener;
    }
    
    /**
     * @return the socket listener
     */
    public SocketListener getSocketListener() {
        return socketListener;
    }
    
    
    /**
     * Registers a topic to this websocket if one isn't already registered under the same topic. Most developers and
     * app use-cases will not need to use this method.
     * @param topic the topic to register
     */
    public void registerTopic(WsTopic<?> topic) {
        this.topics.putIfAbsent(topic.getTopicName().toLowerCase(), topic);
    }
    
    /**
     * Returns a topic manager object from a given topic name.
     * @param topicName the topic name
     * @return the topic associated with the given name
     */
    public WsTopic<?> getTopic(String topicName) {
        return this.topics.get(topicName.toLowerCase());
    }
    
    
    /**
     * Returns a topic object where you can register listeners, subscribe to and unsubscribe from.
     * @return the {@code confirmation} topic object
     */
    public WsTopicConfirmation getTopicConfirmedBlocks() {
        return topicConfirmation;
    }
    
    /**
     * Returns a topic object where you can register listeners, subscribe to and unsubscribe from.
     * @return the {@code active_difficulty} topic object
     */
    public WsTopicActiveDifficulty getTopicActiveDifficulty() {
        return topicActiveDifficulty;
    }
    
    /**
     * Returns a topic object where you can register listeners, subscribe to and unsubscribe from.
     * @return the {@code bootstrap} topic object
     */
    public WsTopicBootstrap getTopicBootstrap() {
        return topicBootstrap;
    }
    
    /**
     * Returns a topic object where you can register listeners, subscribe to and unsubscribe from.
     * @return the {@code stopped_election} topic object
     */
    public WsTopicStoppedElection getTopicStoppedElection() {
        return topicStoppedElection;
    }
    
    /**
     * Returns a topic object where you can register listeners, subscribe to and unsubscribe from.
     * @return the {@code telemetry} topic object
     */
    public WsTopicTelemetry getTopicTelemetry() {
        return topicTelemetry;
    }
    
    /**
     * Returns a topic object where you can register listeners, subscribe to and unsubscribe from.
     * @return the {@code new_unconfirmed_block} topic object
     */
    public WsTopicUnconfirmedBlocks getTopicUnconfirmedBlocks() {
        return topicUnconfirmed;
    }
    
    /**
     * Returns a topic object where you can register listeners, subscribe to and unsubscribe from.
     * @return the {@code vote} topic object
     */
    public WsTopicVote getTopicVote() {
        return topicVote;
    }
    
    /**
     * Returns a topic object where you can register listeners, subscribe to and unsubscribe from.
     * @return the {@code work} topic object
     */
    public WsTopicWork getTopicWork() {
        return topicWork;
    }
    
    
    Map<Long, CountDownLatch> getRequestTrackers() {
        return requestTrackers;
    }
    
    protected void processRequest(JsonObject json) {
        if (!isOpen())
            throw new IllegalStateException("WebSocket is not currently open.");
        
        ws.send(json.toString());
    }
    
    protected boolean processRequestAck(JsonObject json, long timeout) throws InterruptedException {
        if (!isOpen())
            throw new IllegalStateException("WebSocket is not currently open.");
        
        CountDownLatch completionLatch = new CountDownLatch(1);
        long id = nextReqId.getAndIncrement();
        requestTrackers.put(id, completionLatch);
        json.addProperty("id", Long.toString(id, 16));
        json.addProperty("ack", true);
        
        ws.send(json.toString());
        
        if (timeout <= 0) {
            completionLatch.await(); // Indefinite
            return true;
        } else {
            return completionLatch.await(timeout, TimeUnit.MILLISECONDS); // Timeout
        }
    }

}
