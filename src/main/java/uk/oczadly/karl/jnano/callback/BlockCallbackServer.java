package uk.oczadly.karl.jnano.callback;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import uk.oczadly.karl.jnano.callback.httpserver.HttpCallback;
import uk.oczadly.karl.jnano.callback.httpserver.HttpRequest;
import uk.oczadly.karl.jnano.callback.httpserver.HttpServerThread;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.BlockType;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlockCallbackServer {
    
    private final ServerSocket serverSocket;
    private final ExecutorService executorService;
    private final Gson gson;
    private final Set<BlockCallbackListener> listeners = new CopyOnWriteArraySet<>();
    private final HttpCallback callbackListener = new HttpCallbackProcessor();
    
    private HttpServerThread thread;
    
    
    public BlockCallbackServer(int port) throws IOException {
        this(new ServerSocket(port, 0));
    }
    
    public BlockCallbackServer(int port, InetAddress address) throws IOException {
        this(new ServerSocket(port, 0, address));
    }
    
    public BlockCallbackServer(ServerSocket server) {
        this(server, Executors.newFixedThreadPool(100), new GsonBuilder().create()); // Max 100 threads
    }
    
    protected BlockCallbackServer(ServerSocket serverSocket, ExecutorService executorService, Gson gson) {
        this.serverSocket = serverSocket;
        this.executorService = executorService;
        this.gson = gson;
    }
    
    
    /**
     * Registers a new listener to be called when new blocks are processed.
     *
     * @param listener the listener instance to send updates to
     */
    public void registerListener(BlockCallbackListener listener) {
        this.listeners.add(listener);
    }
    
    /**
     * Removes a previously registered block listener.
     *
     * @param listener the listener instance to remove
     * @return whether the listener instance was previously registered
     */
    public boolean unregisterListener(BlockCallbackListener listener) {
        return this.listeners.remove(listener);
    }
    
    /**
     * Notifies the registered block listener instances.
     */
    protected void notifyListeners(BlockData block, String target, InetAddress node) {
        this.listeners.forEach(listener -> listener.onNewBlock(block, target, node));
    }
    
    
    /**
     * @return whether the server is currently running
     */
    public boolean isRunning() {
        return this.thread != null && this.thread.isAlive();
    }
    
    /**
     * Starts the HTTP server and listens for blocks from a configured remote node. This is a non-blocking operation,
     * and is executed from a newly issued thread.
     *
     * @throws IllegalStateException if the server is already running
     */
    public synchronized void start() {
        if (this.isRunning())
            throw new IllegalStateException("Server is currently running");
        
        this.thread = new HttpServerThread(this.serverSocket, callbackListener, this.executorService);
        this.thread.start();
    }
    
    /**
     * Stops the HTTP server from running and frees the network port.
     *
     * @throws IllegalStateException if the server is not currently running
     */
    public synchronized void stop() {
        if (!this.isRunning())
            throw new IllegalStateException("Server is not currently running");
        
        this.thread.interrupt();
        this.thread = null;
    }
    
    
    private class HttpCallbackProcessor implements HttpCallback {
        @Override
        public void onRequest(HttpRequest request) {
            JsonObject json = JsonParser.parseString(request.getBody()).getAsJsonObject();
            
            Block block = gson.fromJson(json.get("block"), Block.class);
            
            BlockData blockData = new BlockData(request.getBody(),
                    NanoAccount.parse(json.get("account").getAsString()),
                    json.get("hash").getAsString(),
                    block,
                    gson.fromJson(json.get("subtype"), BlockType.class),
                    json.has("is_send")
                            ? json.get("is_send").getAsBoolean()
                            : block.getType() == BlockType.SEND,
                    (block.getType().isTransaction() && json.has("amount"))
                            ? json.get("amount").getAsBigInteger()
                            : null);
            
            notifyListeners(blockData, request.getPath(), request.getClientAddr()); // Notify listeners
        }
    }
    
}
