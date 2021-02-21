/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.java_websocket.handshake.ServerHandshake;
import uk.oczadly.karl.jnano.internal.utils.IDRequestTracker;
import uk.oczadly.karl.jnano.internal.utils.ReconnectingWebsocketClient;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.util.NetworkConstants;
import uk.oczadly.karl.jnano.util.workgen.policy.WorkDifficultyPolicy;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A WorkGenerator which uses an external authenticated DPoW/BPoW service over a WebSocket. Note that you will need to
 * obtain and provide credentials to use these third-party services.
 *
 * <p>The WebSocket will attempt to connect as soon as the class is instantiated. If disconnected, the WebSocket
 * will continuously attempt to re-connect every 2 seconds to ensure a readily available connection to the service,
 * and will not close until {@link #shutdown()} is invoked.</p>
 *
 * <p>Instances of this class should be re-used throughout your application, as each instance will spawn a new
 * background thread and websocket connection. This practice also ensures that tasks are queued correctly in the order
 * of request.</p>
 *
 * @see #dpowForNano(String, String)
 * @see #bpowForNano(String, String)
 * @see #bpowForBanano(String, String)
 */
public class DPOWWorkGenerator extends AbstractWorkGenerator {
    
    /**
     * The URI of the secure WebSocket for the Nano <i>Distributed Proof of Work</i> work generation service.
     * @see <a href="https://dpow.nanocenter.org/">https://dpow.nanocenter.org/</a>
     */
    public static final URI URI_DPOW = URI.create("wss://dpow.nanocenter.org/service_ws/");
    
    /**
     * The URI of the secure WebSocket for the Banano <i>BoomPow</i> work generation service.
     * @see <a href="https://bpow.banano.cc/">https://bpow.banano.cc/</a>
     */
    public static final URI URI_BPOW = URI.create("wss://bpow.banano.cc/service_ws/");
    
    
    private final URI uri;
    private final String user, apiKey;
    private final int timeout;
    private final WSHandler websocket;
    private final IDRequestTracker<JsonObject> reqTracker = new IDRequestTracker<>();
    
    /**
     * Creates a new {@code DPOWWorkGenerator} which generates work on the external DPoW service.
     * <p>Use of the static creation methods should be preferred unless using a custom configuration.</p>
     *
     * @param uri     the URI of the service's WebSocket
     * @param user    the API username credential
     * @param apiKey  the API key credential
     * @param timeout the generation timeout, in seconds
     * @param policy  the difficulty policy
     *
     * @see #dpowForNano(String, String)
     * @see #bpowForNano(String, String)
     * @see #bpowForBanano(String, String)
     */
    public DPOWWorkGenerator(URI uri, String user, String apiKey, int timeout, WorkDifficultyPolicy policy) {
        super(policy);
        if (!uri.getScheme().equalsIgnoreCase("ws") && !uri.getScheme().equalsIgnoreCase("wss"))
            throw new IllegalArgumentException("Unsupported URI scheme; must be a websocket.");
        this.uri = uri;
        this.user = user;
        this.apiKey = apiKey;
        this.timeout = timeout;
        this.websocket = new WSHandler(uri, reqTracker);
        this.websocket.connect();
    }
    
    
    /**
     * @return the URI of the service's WebSocket
     */
    public final URI getServiceURI() {
        return uri;
    }
    
    /**
     * @return the username credential
     */
    public final String getUsername() {
        return user;
    }
    
    /**
     * @return the API key credential
     */
    public final String getAPIKey() {
        return apiKey;
    }
    
    /**
     * @return the work generation timeout, in seconds
     */
    public final int getTimeoutDuration() {
        return timeout;
    }
    
    
    @Override
    public void cleanup() {
        try {
            websocket.close();
        } finally {
            super.cleanup();
        }
    }
    
    @Override
    protected final WorkSolution generateWork(HexData root, WorkDifficulty difficulty, RequestContext context)
            throws WorkGenerationException, InterruptedException {
        IDRequestTracker<JsonObject>.Tracker tracker = null;
        try {
            // Await connection
            websocket.awaitConnection(timeout, TimeUnit.SECONDS);
            
            // Send
            tracker = reqTracker.newTracker();
            JsonObject request = buildRequest(tracker.getID(), root, difficulty, context);
            websocket.send(request.toString());
    
            JsonObject response = tracker.await(timeout, TimeUnit.SECONDS); // Await response
            return parseResponse(response); // Parse and return response
        } catch (TimeoutException e) {
            throw new WorkGenerationException("Connection timeout.", e);
        } catch (IDRequestTracker.TrackerExpiredException e) {
            throw new AssertionError("Tracker expired.", e);
        } finally {
            if (tracker != null)
                tracker.expire();
        }
    }
    
    
    /**
     * Build the request body (JSON).
     * @param id         the request ID
     * @param root       the root hash
     * @param difficulty the difficulty
     * @param context    the generation request context
     * @return the created request body
     */
    protected JsonObject buildRequest(String id, HexData root, WorkDifficulty difficulty, RequestContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("id",         id);
        json.addProperty("user",       user);
        json.addProperty("api_key",    apiKey);
        json.addProperty("timeout",    timeout);
        json.addProperty("hash",       root.toHexString());
        json.addProperty("difficulty", difficulty.getAsHexadecimal());
        context.getAccount().ifPresent(acc -> json.addProperty("account", acc.toAddress())); // For precaching
        return json;
    }
    
    /**
     * Parse the response from the server.
     * @param json the response data
     * @return the parsed work response
     * @throws RemoteException if an error is returned
     */
    protected WorkSolution parseResponse(JsonObject json) throws RemoteException {
        if (json.has("work")) {
            return new WorkSolution(json.get("work").getAsString());
        } else if (json.has("error")) {
            throw new RemoteException(json.get("error").getAsString());
        }
        throw new RemoteException("Unexpected response.");
    }
    
    
    /**
     * Creates a {@code DPOWWorkGenerator} for the DPoW service provided by the NanoCenter, using a timeout of {@code
     * 5} seconds using the default Nano difficulty thresholds.
     *
     * @param user   the username credential
     * @param apiKey the API key credential
     * @return the created generator
     *
     * @see #URI_DPOW
     * @see NetworkConstants#NANO
     * @see <a href="https://dpow.nanocenter.org/">https://dpow.nanocenter.org/</a>
     */
    public static DPOWWorkGenerator dpowForNano(String user, String apiKey) {
        return dpowForNano(user, apiKey, 5);
    }
    
    /**
     * Creates a {@code DPOWWorkGenerator} for the DPoW service provided by the NanoCenter using the default Nano
     * difficulty thresholds.
     *
     * @param user    the username credential
     * @param apiKey  the API key credential
     * @param timeout the generation timeout, in seconds
     * @return the created generator
     *
     * @see #URI_DPOW
     * @see NetworkConstants#NANO
     * @see <a href="https://dpow.nanocenter.org/">https://dpow.nanocenter.org/</a>
     */
    public static DPOWWorkGenerator dpowForNano(String user, String apiKey, int timeout) {
        return new DPOWWorkGenerator(URI_DPOW, user, apiKey, timeout, NetworkConstants.NANO.getWorkDifficulties());
    }
    
    /**
     * Creates a {@code DPOWWorkGenerator} for the BoomPow service, using a timeout of {@code 5} seconds using the
     * default Nano difficulty thresholds.
     *
     * @param user   the username credential
     * @param apiKey the API key credential
     * @return the created generator
     *
     * @see #URI_BPOW
     * @see NetworkConstants#NANO
     * @see <a href="https://bpow.banano.cc/">https://bpow.banano.cc/</a>
     */
    public static DPOWWorkGenerator bpowForNano(String user, String apiKey) {
        return bpowForNano(user, apiKey, 5);
    }
    
    /**
     * Creates a {@code DPOWWorkGenerator} for the BoomPow service, using the default Nano difficulty thresholds.
     *
     * @param user    the username credential
     * @param apiKey  the API key credential
     * @param timeout the generation timeout, in seconds
     * @return the created generator
     *
     * @see #URI_BPOW
     * @see NetworkConstants#NANO
     * @see <a href="https://bpow.banano.cc/">https://bpow.banano.cc/</a>
     */
    public static DPOWWorkGenerator bpowForNano(String user, String apiKey, int timeout) {
        return new DPOWWorkGenerator(URI_BPOW, user, apiKey, timeout, NetworkConstants.NANO.getWorkDifficulties());
    }
    
    /**
     * Creates a {@code DPOWWorkGenerator} for the BoomPow service, using a timeout of {@code 5} seconds using the
     * default Banano difficulty thresholds.
     *
     * @param user   the username credential
     * @param apiKey the API key credential
     * @return the created generator
     *
     * @see #URI_BPOW
     * @see NetworkConstants#BANANO
     * @see <a href="https://bpow.banano.cc/">https://bpow.banano.cc/</a>
     */
    public static DPOWWorkGenerator bpowForBanano(String user, String apiKey) {
        return bpowForBanano(user, apiKey, 5);
    }
    
    /**
     * Creates a {@code DPOWWorkGenerator} for the BoomPow service, using the default Banano difficulty thresholds.
     *
     * @param user    the username credential
     * @param apiKey  the API key credential
     * @param timeout the generation timeout, in seconds
     * @return the created generator
     *
     * @see #URI_BPOW
     * @see NetworkConstants#BANANO
     * @see <a href="https://bpow.banano.cc/">https://bpow.banano.cc/</a>
     */
    public static DPOWWorkGenerator bpowForBanano(String user, String apiKey, int timeout) {
        return new DPOWWorkGenerator(URI_BPOW, user, apiKey, timeout, NetworkConstants.BANANO.getWorkDifficulties());
    }
    
    
    /**
     * Thrown when a remote error occurs when requesting or generating work.
     */
    public static class RemoteException extends WorkGenerationException {
        RemoteException(String message) {
            super(message);
        }
    }
    
    
    private static class WSHandler extends ReconnectingWebsocketClient {
        private final IDRequestTracker<JsonObject> tracker;
        private final Lock notifyLock = new ReentrantLock();
        private final Condition notifyCond = notifyLock.newCondition();
        private volatile boolean open = false;
        
        public WSHandler(URI serverUri, IDRequestTracker<JsonObject> tracker) {
            super(serverUri, 2000);
            this.tracker = tracker;
        }
        
        @Override
        public void onClose(int code, String reason, boolean remote, boolean reconnectAttempt) {
            open = false;
        }
        
        @Override
        public void onOpen(ServerHandshake handshakedata, boolean reconnect) {
            notifyLock.lock();
            open = true;
            notifyCond.signalAll();
            notifyLock.unlock();
        }
    
        @Override
        public void onMessage(String message) {
            JsonObject json = JsonParser.parseString(message).getAsJsonObject();
            tracker.complete(json.get("id").getAsString(), json);
        }
    
        @Override
        public void onError(Exception ex) {}
    
        
        public void awaitConnection(long time, TimeUnit unit) throws InterruptedException, TimeoutException {
            try {
                notifyLock.lock();
                if (open) return; // Already open
                if (!notifyCond.await(time, unit))
                    throw new TimeoutException("WebSocket was not connected.");
            } finally {
                notifyLock.unlock();
            }
        }
    }
    
}
