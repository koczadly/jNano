package in.bigdolph.jnano.rpc.callback.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import in.bigdolph.jnano.rpc.callback.BlockCallbackListener;
import in.bigdolph.jnano.rpc.callback.BlockInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.HashSet;
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
        this(server, Executors.newCachedThreadPool(), new GsonBuilder().create());
    }
    
    protected BlockCallbackServer(ServerSocket serverSocket, ExecutorService executorService, Gson gson) {
        this.serverSocket = serverSocket;
        this.executorService = executorService;
        this.gson = gson;
    }
    
    
    
    public void registerListener(BlockCallbackListener listener) {
        this.listeners.add(listener);
    }
    
    public void unregisterListener(BlockCallbackListener listener) {
        this.listeners.remove(listener);
    }
    
    protected void notifyListeners(BlockInfo block, String target, InetAddress node) {
        this.listeners.forEach(listener -> listener.onNewBlock(block, target, node));
    }
    
    
    public boolean isRunning() {
        return this.thread != null && this.thread.isAlive();
    }
    
    public void start() {
        if(this.isRunning()) throw new IllegalStateException("Server is currently running");
        this.thread = new HttpServerThread(this.serverSocket, this, this.executorService);
        this.thread.start();
    }
    
    public void stop() {
        if(!this.isRunning()) throw new IllegalStateException("Server is not currently running");
        this.thread.interrupt();
        this.thread = null;
    }
    
    
    protected void handleRequest(HttpRequest request) {
        //Deserialize JSON
        BlockInfo blockInfo = this.gson.fromJson(request.getBody(), BlockInfo.class);
        
        //Notify listeners
        this.notifyListeners(blockInfo, request.getPath(), request.getClientAddr());
    }
    
}
