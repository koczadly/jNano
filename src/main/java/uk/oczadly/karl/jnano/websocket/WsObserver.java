/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket;

import com.google.gson.JsonObject;

public interface WsObserver {
    
    /** A simple implementation that outputs status updates to the console. */
    WsObserver DEFAULT = new WsObserver() {
        @Override
        public void onOpen(int httpStatus) {
            System.out.println("WebSocket connected.");
        }
        
        @Override
        public void onClose(int code, String reason, boolean remote) {
            System.out.println("WebSocket closed.");
        }
        
        @Override
        public void onSocketError(Exception ex) {
            System.err.println("WebSocket connection exception:");
            ex.printStackTrace();
        }
    };
    
    
    /**
     * Executed when the WebSocket is opened.
     * @param httpStatus the HTTP status code
     */
    void onOpen(int httpStatus);
    
    /**
     * Executed when the WebSocket receives a new message.
     * @param json    the JSON message
     * @param handled true if the message was handled by a listener
     */
    default void onMessage(JsonObject json, boolean handled) {}
    
    /**
     * Executed when the WebSocket is closed.
     * @param code   the reason code
     * @param reason the reason message
     * @param remote true if the closure was initiated by the remote WebSocket
     */
    void onClose(int code, String reason, boolean remote);
    
    /**
     * Executed when an exception occurs with the connection.
     * @param ex the exception
     */
    void onSocketError(Exception ex);
    
    /**
     * Executed when an uncaught exception occurs within one of the listeners.
     * @param ex the exception
     */
    default void onHandlerError(Exception ex) {
        System.err.println("Uncaught error within WebSocket:");
        ex.printStackTrace();
    }

}
