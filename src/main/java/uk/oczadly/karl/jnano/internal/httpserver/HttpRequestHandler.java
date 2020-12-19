/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.internal.httpserver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpRequestHandler implements Runnable {
    
    private Socket socket;
    private HttpCallback callback;
    
    public HttpRequestHandler(Socket socket, HttpCallback callback) {
        this.socket = socket;
        this.callback = callback;
    }
    
    
    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(),
                    StandardCharsets.UTF_8));
            
            String[] request = reader.readLine().split(" ");
            if (!request[0].equalsIgnoreCase("POST")) return; // Ignore non-post
            
            int contentLength = -1;
            
            String s;
            while ((s = reader.readLine()) != null) {
                if (s.equals("")) {
                    break; // End of headers
                } else if (contentLength == -1) {
                    String[] header = s.split(":", 2);
                    if (header[0].trim().equalsIgnoreCase("content-length")) {
                        contentLength = Integer.parseInt(header[1].trim());
                    }
                }
            }
            if (contentLength == -1) return; // Couldn't read length
            
            // Read request body
            char[] buffer = new char[contentLength];
            reader.read(buffer, 0, contentLength);
            
            // Return OK
            BufferedOutputStream output = new BufferedOutputStream(this.socket.getOutputStream());
            output.write("HTTP/1.1 200 OK\r\n\r\n".getBytes(StandardCharsets.UTF_8));
            output.flush();
            output.close();
            
            // Close socket
            this.socket.close();
            
            HttpRequest requestData = new HttpRequest(this.socket.getInetAddress(), request.length > 2 ? request[1]
                    : "", contentLength, String.valueOf(buffer));
            
            // Notify callback
            callback.onRequest(requestData);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close socket
            try {
                if (!this.socket.isClosed()) this.socket.close();
            } catch (IOException ignored) {
            }
        }
    }
    
}