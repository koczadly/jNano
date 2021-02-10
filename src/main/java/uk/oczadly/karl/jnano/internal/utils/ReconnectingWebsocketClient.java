/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.internal.utils;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;

/**
 * A websocket which automatically reconnects upon disconnection.
 */
public abstract class ReconnectingWebsocketClient extends WebSocketClient {
    
    private final int reconDelay;
    private volatile boolean isReconnecting, manuallyClosed;
    private final Object reconMonitor = new Object();
    
    public ReconnectingWebsocketClient(URI serverUri, int reconDelay) {
        super(serverUri);
        this.reconDelay = reconDelay;
    }
    
    public ReconnectingWebsocketClient(URI serverUri, Draft protocolDraft, int reconDelay) {
        super(serverUri, protocolDraft);
        this.reconDelay = reconDelay;
    }
    
    public ReconnectingWebsocketClient(URI serverUri, Map<String, String> httpHeaders, int reconDelay) {
        super(serverUri, httpHeaders);
        this.reconDelay = reconDelay;
    }
    
    public ReconnectingWebsocketClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders,
                                       int reconDelay) {
        super(serverUri, protocolDraft, httpHeaders);
        this.reconDelay = reconDelay;
    }
    
    public ReconnectingWebsocketClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders,
                                       int connectTimeout, int reconDelay) {
        super(serverUri, protocolDraft, httpHeaders, connectTimeout);
        this.reconDelay = reconDelay;
    }
    
    
    public final boolean isReconnecting() {
        return isReconnecting;
    }
    
    @Override
    public final void onOpen(ServerHandshake handshakedata) {
        boolean reconTemp = isReconnecting;
        isReconnecting = false;
        onOpen(handshakedata, reconTemp);
    }
    
    @Override
    public final void onClose(int code, String reason, boolean remote) {
        onClose(code, reason, remote, isReconnecting);
        if (remote || code != 1000)
            doReconnect();
    }
    
    @Override
    public void close() {
        manuallyClosed = true;
        super.close();
    }
    
    public abstract void onOpen(ServerHandshake handshakedata, boolean reconnect);
    
    public abstract void onClose(int code, String reason, boolean remote, boolean reconnectAttempt);
    
    
    protected final void doReconnect() {
        if (!isReconnecting) {
            synchronized (reconMonitor) {
                if (!isReconnecting) {
                    isReconnecting = true;
                    Thread reconThread = new Thread(new ReconTask());
                    reconThread.setDaemon(true);
                    reconThread.start();
                }
            }
        }
    }
    
    private class ReconTask implements Runnable {
        @Override
        public void run() {
            try {
                do {
                    Thread.sleep(reconDelay);
                } while (!manuallyClosed && !reconnectBlocking());
            } catch (InterruptedException ignored) {}
            isReconnecting = false;
        }
    }
    
}
