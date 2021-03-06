/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.callback;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import uk.oczadly.karl.jnano.internal.JNC;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.internal.httpserver.HttpCallback;
import uk.oczadly.karl.jnano.internal.httpserver.HttpRequest;
import uk.oczadly.karl.jnano.internal.httpserver.HttpServerThread;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.BlockType;
import uk.oczadly.karl.jnano.model.block.StateBlockSubType;

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
     * @param block the block data
     * @param target the target URI path
     * @param node the node's external address
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
            JsonObject json = JNH.parseJson(request.getBody());
            
            BlockType subtype = gson.fromJson(json.get("subtype"), BlockType.class);
            
            JsonObject blockJson = JNH.parseJson(json.get("block").getAsString());
            // Manually add subtype property for parsing (not included automatically)
            if (blockJson.get("type").getAsString().equals(BlockType.STATE.getProtocolName()))
                blockJson.addProperty("subtype", StateBlockSubType.getFromLegacyType(subtype).getProtocolName());
            Block block = JNC.BLOCK_DESERIALIZER.deserialize(blockJson);
            
            BlockData blockData = new BlockData(request.getBody(),
                    NanoAccount.parse(json.get("account").getAsString()),
                    JNH.getJson(json, "hash", HexData::new),
                    block,
                    subtype,
                    json.has("is_send")
                            ? json.get("is_send").getAsBoolean()
                            : block.getType() == BlockType.SEND,
                    json.has("amount")
                            ? NanoAmount.valueOfRaw(json.get("amount").getAsString())
                            : null);
            
            notifyListeners(blockData, request.getPath(), request.getClientAddr()); // Notify listeners
        }
    }
    
}
