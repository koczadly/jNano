package uk.oczadly.karl.jnano.callback.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class HttpServerThread extends Thread {
    
    private ServerSocket socket;
    private BlockCallbackServer callbackServer;
    private ExecutorService executorService;
    
    public HttpServerThread(ServerSocket socket, BlockCallbackServer callbackServer, ExecutorService executorService) {
        super("http-server-thread");
        this.socket = socket;
        this.callbackServer = callbackServer;
        this.executorService = executorService;
    }
    
    
    
    @Override
    public void run() {
        while(!Thread.interrupted()) {
            try {
                Socket socket = this.socket.accept(); //Listen for requests
                socket.setSoTimeout(20000);
                socket.setKeepAlive(false);
                this.executorService.submit(new HttpClient(socket, this.callbackServer)); //Start new thread for client
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}
