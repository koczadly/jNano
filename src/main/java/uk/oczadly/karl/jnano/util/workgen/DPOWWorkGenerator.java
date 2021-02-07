/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import uk.oczadly.karl.jnano.internal.HTTPUtil;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.util.NetworkConstants;
import uk.oczadly.karl.jnano.util.workgen.policy.WorkDifficultyPolicy;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A WorkGenerator which uses an external authenticated DPoW/BPoW service. Note that you will need to obtain and
 * provide credentials to use these external services.
 *
 * <p>Instances of this class should be re-used throughout your application, as each instance will spawn new
 * background threads. This practice also ensures that tasks are queued correctly in the order of request.</p>
 *
 * @see #forNano(String, String)
 * @see #forBanano(String, String)
 */
public final class DPOWWorkGenerator extends AbstractWorkGenerator {
    
    /**
     * The secure endpoint URL for the Nano <i>Distributed Proof of Work</i> work generation service.
     * @see <a href="https://dpow.nanocenter.org/">https://dpow.nanocenter.org/</a>
     */
    public static final URL URL_NANO_DPOW = JNH.unchecked(() -> new URL("https://dpow.nanocenter.org/service/"));
    
    /**
     * The secure endpoint URL for the Banano <i>BoomPow</i> work generation service.
     * @see <a href="https://bpow.banano.cc/">https://bpow.banano.cc/</a>
     */
    public static final URL URL_BANANO_BPOW = JNH.unchecked(() -> new URL("https://bpow.banano.cc/service/"));
    
    
    private final URL url;
    private final String user, apiKey;
    private final int timeout;
    
    /**
     * Creates a new {@code DPOWWorkGenerator} which generates work on the external DPoW service.
     * @param url     the URL of the service
     * @param user    the username credential
     * @param apiKey  the API key credential
     * @param timeout the generation timeout, in seconds
     * @param policy  the difficulty policy
     *
     * @see #forNano(String, String)
     * @see #forBanano(String, String)
     */
    public DPOWWorkGenerator(URL url, String user, String apiKey, int timeout, WorkDifficultyPolicy policy) {
        super(policy);
        this.url = url;
        this.user = user;
        this.apiKey = apiKey;
        this.timeout = timeout;
    }
    
    
    /**
     * @return the URL of the service
     */
    public URL getServiceURL() {
        return url;
    }
    
    /**
     * @return the username credential
     */
    public String getUsername() {
        return user;
    }
    
    /**
     * @return the API key credential
     */
    public String getAPIKey() {
        return apiKey;
    }
    
    /**
     * @return the work generation timeout, in seconds
     */
    public int getTimeoutDuration() {
        return timeout;
    }
    
    
    @Override
    protected WorkSolution generateWork(HexData root, WorkDifficulty difficulty) throws Exception {
        // Build request
        // TODO: support 'account' for precaching when block is passed
        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("user",       user);
        requestJson.addProperty("api_key",    apiKey);
        requestJson.addProperty("timeout",    timeout);
        requestJson.addProperty("hash",       root.toHexString());
        requestJson.addProperty("difficulty", difficulty.getAsHexadecimal());
        
        // Make request
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setReadTimeout(timeout * 1000 + 500);
        con.setDoInput(true);
        con.setDoOutput(true);
        
        String responseStr = HTTPUtil.request(con, requestJson.toString());
        JsonObject response = JsonParser.parseString(responseStr).getAsJsonObject();
        
        if (response.has("error")) {
            // Error
            throw new RemoteException(response.get("error").getAsString(), response.has("timeout"));
        } else {
            // Success
            return new WorkSolution(response.get("work").getAsString());
        }
    }
    
    
    /**
     * Creates a {@code DPOWWorkGenerator} for the DPoW service provided by the NanoCenter, using a timeout of {@code
     * 5} seconds using the default Nano difficulty thresholds.
     *
     * @param user   the username credential
     * @param apiKey the API key credential
     * @return the created generator
     * @see #URL_NANO_DPOW
     * @see <a href="https://dpow.nanocenter.org/">https://dpow.nanocenter.org/</a>
     */
    public static DPOWWorkGenerator forNano(String user, String apiKey) {
        return forNano(user, apiKey, 5);
    }
    
    /**
     * Creates a {@code DPOWWorkGenerator} for the DPoW service provided by the NanoCenter using the default Nano
     * difficulty thresholds.
     *
     * @param user    the username credential
     * @param apiKey  the API key credential
     * @param timeout the generation timeout, in seconds
     * @return the created generator
     * @see #URL_NANO_DPOW
     * @see <a href="https://dpow.nanocenter.org/">https://dpow.nanocenter.org/</a>
     */
    public static DPOWWorkGenerator forNano(String user, String apiKey, int timeout) {
        return new DPOWWorkGenerator(URL_NANO_DPOW, user, apiKey, timeout,
                NetworkConstants.NANO.getWorkDifficulties());
    }
    
    /**
     * Creates a {@code DPOWWorkGenerator} for the BoomPow service, using a timeout of {@code 5} seconds using the
     * default Banano difficulty thresholds.
     *
     * @param user   the username credential
     * @param apiKey the API key credential
     * @return the created generator
     * @see #URL_BANANO_BPOW
     * @see <a href="https://bpow.banano.cc/">https://bpow.banano.cc/</a>
     */
    public static DPOWWorkGenerator forBanano(String user, String apiKey) {
        return forBanano(user, apiKey, 5);
    }
    
    /**
     * Creates a {@code DPOWWorkGenerator} for the BoomPow service, using the default Banano difficulty thresholds.
     *
     * @param user    the username credential
     * @param apiKey  the API key credential
     * @param timeout the generation timeout, in seconds
     * @return the created generator
     * @see #URL_BANANO_BPOW
     * @see <a href="https://bpow.banano.cc/">https://bpow.banano.cc/</a>
     */
    public static DPOWWorkGenerator forBanano(String user, String apiKey, int timeout) {
        return new DPOWWorkGenerator(URL_BANANO_BPOW, user, apiKey, timeout,
                NetworkConstants.BANANO.getWorkDifficulties());
    }
    
    
    /**
     * Thrown when a remote error occurs when requesting or generating work.
     */
    public static class RemoteException extends Exception {
        final boolean timeout;
        
        RemoteException(String message, boolean timeout) {
            super(message);
            this.timeout = timeout;
        }
    
        /**
         * @return true if the cause was a timeout
         */
        public boolean isTimeout() {
            return timeout;
        }
    }
    
}
