package in.bigdolph.jnano.rpc.callback;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CallbackServer {
    
    private ServerSocket server;
    private ServerThread thread;
    
    private Set<CallbackBlockListener> listeners = ConcurrentHashMap.newKeySet();
    private Gson gson;
    
    
    public CallbackServer(int port) throws IOException {
        this(new ServerSocket(port, 0));
    }
    
    public CallbackServer(int port, InetAddress address) throws IOException {
        this(new ServerSocket(port, 0, address));
    }
    
    public CallbackServer(ServerSocket server) {
        this(server, new GsonBuilder().create());
    }
    
    protected CallbackServer(ServerSocket server, Gson gson) {
        this.server = server;
        this.gson = gson;
    }
    
    
    
    public void registerListener(CallbackBlockListener listener) {
        this.listeners.add(listener);
    }
    
    public void unregisterListener(CallbackBlockListener listener) {
        this.listeners.remove(listener);
    }
    
    protected void notifyListeners(InetAddress nodeAddress, String context, CallbackBlock block) {
        this.listeners.forEach(listener -> listener.onNewBlock(nodeAddress, context, block));
    }
    
    
    public void start() {
        if(this.thread != null && this.thread.isAlive()) throw new IllegalStateException("Server is currently running");
        this.thread = new ServerThread();
        this.thread.start();
    }
    
    public void stop() {
        if(this.thread == null || !this.thread.isAlive()) throw new IllegalStateException("Server is not currently running");
        this.thread.interrupt();
    }
    
    
    
    private class ServerThread extends Thread {
        
        private ExecutorService executorService = Executors.newCachedThreadPool();
        
        public ServerThread() {
            super("callback-http-server");
        }
        
        
        @Override
        public void run() {
            while(!this.isInterrupted()) {
                try {
                    Socket socket = CallbackServer.this.server.accept(); //Listen for requests
                    socket.setSoTimeout(20000);
                    socket.setKeepAlive(false);
                    socket.setReuseAddress(true);
                    this.executorService.submit(new HttpClient(socket)); //Start new thread for client
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        @Override
        public void interrupt() {
            try {
                CallbackServer.this.server.close();
            } catch (IOException e) {}
            super.interrupt();
        }
        
    }
    
    
    private class HttpClient implements Runnable {
    
        private Socket socket;
        
        public HttpClient(Socket socket) {
            this.socket = socket;
        }
        
        
        @Override
        public void run() {
            try {
                //Read contents
                BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "utf-8"));
                
                String[] request = reader.readLine().split(" ");
                if(!request[0].equalsIgnoreCase("POST")) {
                    this.socket.close();
                    return; //Ignore non-post
                }
                
                int contentLength = -1;
                
                String s;
                while((s = reader.readLine()) != null) {
                    if(s.equals("")) {
                        break; //End of headers
                    } else if(contentLength == -1) {
                        String[] header = s.split(":", 2);
                        if(header[0].trim().equalsIgnoreCase("content-length")) {
                            contentLength = Integer.parseInt(header[1].trim());
                        }
                    }
                }
                if(contentLength == -1) {
                    this.socket.close();
                    return; //Couldn't read length
                }
                
                //Read request body
                char[] buffer = new char[contentLength];
                reader.read(buffer, 0, contentLength);
                String body = String.valueOf(buffer);
                
                //Return OK
                DataOutputStream output = new DataOutputStream(this.socket.getOutputStream());
                output.writeUTF("HTTP/1.1 200 OK");
                output.flush();
                
                //Close socket
                this.socket.close();
                
                //Deserialize JSON
                CallbackBlock block = CallbackServer.this.gson.fromJson(body, CallbackBlock.class);
                
                //Notify listeners
                CallbackServer.this.notifyListeners(socket.getInetAddress(), request[1], block);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }
    
}
