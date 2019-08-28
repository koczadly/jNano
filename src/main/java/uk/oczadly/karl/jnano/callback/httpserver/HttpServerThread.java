package uk.oczadly.karl.jnano.callback.httpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class HttpServerThread extends Thread {
    
    private ServerSocket socket;
    private HttpCallback callbackListener;
    private ExecutorService executorService;
    
    
    public HttpServerThread(ServerSocket socket, HttpCallback callbackListener, ExecutorService executorService) {
        super("http-server-thread");
        this.socket = socket;
        this.callbackListener = callbackListener;
        this.executorService = executorService;
    }
    
    
    @Override
    public void run() {
        while(!Thread.interrupted()) {
            try {
                Socket socket = this.socket.accept(); //Listen for requests
                
                socket.setSoTimeout(10000);
                socket.setKeepAlive(false);
                
                this.executorService.submit(
                        new HttpRequestHandler(socket, this.callbackListener)); //Start new thread for client
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}
