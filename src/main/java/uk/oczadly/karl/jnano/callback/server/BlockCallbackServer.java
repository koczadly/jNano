package uk.oczadly.karl.jnano.callback.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.oczadly.karl.jnano.callback.BlockCallbackListener;
import uk.oczadly.karl.jnano.callback.BlockInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlockCallbackServer {
    
    private final ServerSocket serverSocket;
    private final ExecutorService executorService;
    private final Gson gson;
    private final Set<BlockCallbackListener> listeners = Collections.newSetFromMap(new ConcurrentHashMap<>());
    
    private HttpServerThread thread;
    
    
    public BlockCallbackServer(int port) throws IOException {
        this(new ServerSocket(port, 0));
    }
    
    public BlockCallbackServer(int port, InetAddress address) throws IOException {
        this(new ServerSocket(port, 0, address));
    }
    
    public BlockCallbackServer(ServerSocket server) {
        this(server, Executors.newFixedThreadPool(100), new GsonBuilder().create()); //Max 100 threads
    }
    
    protected BlockCallbackServer(ServerSocket serverSocket, ExecutorService executorService, Gson gson) {
        this.serverSocket = serverSocket;
        this.executorService = executorService;
        this.gson = gson;
    }
    
    
    /**
     * Registers a new listener to be called when new blocks are captured.
     *
     * @param listener  the listener instance to send updates to
     */
    public void registerListener(BlockCallbackListener listener) {
        this.listeners.add(listener);
    }
    
    /**
     * Removes a previously registered listener.
     *
     * @param listener  the listener instance to remove
     * @return whether the listener instance was previously registered
     */
    public boolean unregisterListener(BlockCallbackListener listener) {
        return this.listeners.remove(listener);
    }
    
    /**
     * Notifies the registered listener instances
     */
    protected void notifyListeners(BlockInfo block, String target, InetAddress node) {
        this.listeners.forEach(listener -> listener.onNewBlock(block, target, node));
    }
    
    
    /**
     * @return whether the server is currently running
     */
    public boolean isRunning() {
        return this.thread != null && this.thread.isAlive();
    }
    
    /**
     * Starts the HTTP server and listens for blocks.
     * @throws IllegalStateException if the server is already running
     */
    public void start() {
        if(this.isRunning()) throw new IllegalStateException("Server is currently running");
        this.thread = new HttpServerThread(this.serverSocket, this, this.executorService);
        this.thread.start();
    }
    
    /**
     * Stops the HTTP server from running and frees the port.
     * @throws IllegalStateException if the server is not currently running
     */
    public void stop() {
        if(!this.isRunning()) throw new IllegalStateException("Server is not currently running");
        this.thread.interrupt();
        this.thread = null;
    }
    
    
    /**
     * Handles an incoming block request from the HTTP client and broadcasts it to the listeners.
     *
     * @param request   the HTTP request information
     */
    protected void handleRequest(HttpRequest request) {
        //Deserialize JSON
        BlockInfo blockInfo = this.gson.fromJson(request.getBody(), BlockInfo.class);
        
        //Notify listeners
        this.notifyListeners(blockInfo, request.getPath(), request.getClientAddr());
    }
    
}
