/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.util;

import com.google.gson.JsonObject;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.rpc.HttpRequestExecutor;
import uk.oczadly.karl.jnano.rpc.JsonRequestSerializer;
import uk.oczadly.karl.jnano.rpc.JsonResponseDeserializer;
import uk.oczadly.karl.jnano.rpc.RpcQueryNode;
import uk.oczadly.karl.jnano.rpc.exception.RpcCommandNotAllowedException;
import uk.oczadly.karl.jnano.rpc.exception.RpcException;
import uk.oczadly.karl.jnano.rpc.exception.RpcThirdPartyException;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;
import uk.oczadly.karl.jnano.rpc.util.wallet.LocalRpcWalletAccount;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class contains a set of static initializer methods for publicly available third-party RPC endpoints.
 *
 * <p>Consider using the {@link LocalRpcWalletAccount} utility class when managing accounts and using a third-party
 * RPC provider to refrain from exposing your seeds and private keys.</p>
 */
public final class RpcServiceProviders {
    private RpcServiceProviders() {}
    
    private static final String URL_NATRIUM   = "https://app.natrium.io/api";
    private static final String URL_WENANO    = "https://rpc.wenano.net/api/node-api";
    private static final String URL_NANEX_CC  = "https://api.nanex.cc";
    private static final String URL_NINJA     = "https://mynano.ninja/api/node";
    private static final String URL_NANOS_CC  = "https://proxy.nanos.cc/proxy";
    private static final String URL_SOMENANO  = "https://node.somenano.com/proxy";
    private static final String URL_POWERNODE = "https://proxy.powernode.cc/proxy";
    
    
    /**
     * Creates an {@code RpcQueryNode} object which connects to the <a href="https://natrium.io">Natrium</a> API
     * endpoint securely via {@code https}.
     *
     * <p>This endpoint is not officially public, and may be lacking in documentation or compatibility. Some commands
     * may be unavailable or restricted, and no guarantees on the reliability or security are provided.
     * <strong>You should never send any private keys, seeds or passwords to third-party services.</strong></p>
     *
     * @return the configured RPC node object
     */
    public static RpcQueryNode natrium() {
        return new RpcQueryNode(JNH.parseURL(URL_NATRIUM));
    }
    
    /**
     * Creates an {@code RpcQueryNode} object which connects to the <a href="https://wenano.net">WeNano</a> API endpoint
     * securely via {@code https}.
     *
     * <p>This endpoint is not officially public, and may be lacking in documentation or compatibility. Some commands
     * may be unavailable or restricted, and no guarantees on the reliability or security are provided.
     * <strong>You should never send any private keys, seeds or passwords to third-party services.</strong></p>
     *
     * @return the configured RPC node object
     */
    public static RpcQueryNode wenano() {
        return new RpcQueryNode(JNH.parseURL(URL_WENANO));
    }
    
    /**
     * Creates an {@code RpcQueryNode} object which connects to the <a href="https://nanex.cc">nanex.cc</a> API
     * endpoint securely via {@code https}.
     *
     * <p>Some commands may be unavailable or restricted, and no guarantees on the reliability or security are provided.
     * <strong>You should never send any private keys, seeds or passwords to third-party services.</strong></p>
     *
     * @return the configured RPC node object
     *
     * @see <a href="http://nanex.cc/nano-rpc-api-description">http://nanex.cc/nano-rpc-api-description</a>
     */
    public static RpcQueryNode nanex() {
        return new RpcQueryNode(JNH.parseURL(URL_NANEX_CC));
    }
    
    /**
     * Creates an {@code RpcQueryNode} object which connects to the <a href="https://mynano.ninja/api/node">
     * mynano.ninja</a> API endpoint securely via {@code https}.
     *
     * <p><strong>Note:</strong> this service only permits {@code 500} free requests each hour.</p>
     *
     * <p>Some commands may be unavailable or restricted, and no guarantees on the reliability or security are provided.
     * <strong>You should never send any private keys, seeds or passwords to third-party services.</strong></p>
     *
     * @return the configured RPC node object
     *
     * @see #myNanoNinja(String)
     * @see <a href="https://mynano.ninja/api/node">https://mynano.ninja/api/node</a>
     */
    public static RpcQueryNode myNanoNinja() {
        return myNanoNinja(null);
    }
    
    /**
     * Creates an {@code RpcQueryNode} object which connects to the <a href="https://mynano.ninja/api/node">
     * mynano.ninja</a> API endpoint securely via {@code https}.
     *
     * <p><strong>Note:</strong> this service requires an API key to use, and charges for each query.</p>
     *
     * <p>Some commands may be unavailable or restricted, and no guarantees on the reliability or security are provided.
     * <strong>You should never send any private keys, seeds or passwords to third-party services.</strong></p>
     *
     * @param apiKey your API key for authentication
     * @return the configured RPC node object
     *
     * @see #myNanoNinja()
     * @see <a href="https://mynano.ninja/api/node">https://mynano.ninja/api/node</a>
     */
    public static RpcQueryNode myNanoNinja(String apiKey) {
        return new RpcQueryNode.Builder()
                .setRequestExecutor(new ExecutorWithAuth(JNH.parseURL(URL_NINJA), apiKey))
                .setDeserializer(new JsonResponseDeserializer() {
                    @Override
                    protected <R extends RpcResponse> R deserialize(JsonObject json, Class<R> responseClass)
                            throws RpcException {
                        if (json.size() == 1 && json.has("message")) {
                            String message = json.get("message").getAsString();
                            switch (message) {
                                case "Too Many Requests":
                                    throw new RpcThirdPartyException.TokensExhaustedException(
                                            "Free requests exhausted.", message);
                                case "Insufficient funds / User not found":
                                    throw new RpcThirdPartyException.TokensExhaustedException(
                                            "Not enough API access tokens.", message);
                                case "Action is not supported":
                                    throw new RpcCommandNotAllowedException(message);
                                default:
                                    throw new RpcThirdPartyException(message);
                            }
                        }
                        return super.deserialize(json, responseClass);
                    }
                }).build();
    }
    
    /**
     * Creates an {@code RpcQueryNode} object which connects to the <a href="https://api.nanos.cc/">nanos.cc</a> API
     * endpoint securely via {@code https}.
     *
     * <p><strong>Note:</strong> this service only permits {@code 5000} free requests each day.</p>
     *
     * <p>Some commands may be unavailable or restricted, and no guarantees on the reliability or security are provided.
     * <strong>You should never send any private keys, seeds or passwords to third-party services.</strong></p>
     *
     * @return the configured RPC node object
     *
     * @see #nanos(String)
     * @see <a href="https://api.nanos.cc/">https://api.nanos.cc/</a>
     */
    public static RpcQueryNode nanos() {
        return nanos(null);
    }
    
    /**
     * Creates an {@code RpcQueryNode} object which connects to the <a href="https://api.nanos.cc/">nanos.cc</a> API
     * endpoint securely via {@code https}.
     *
     * <p><strong>Note:</strong> this service requires an API key to use, and charges for each query.</p>
     *
     * <p>Some commands may be unavailable or restricted, and no guarantees on the reliability or security are provided.
     * <strong>You should never send any private keys, seeds or passwords to third-party services.</strong></p>
     *
     * @param apiKey your API key for authentication
     * @return the configured RPC node object
     *
     * @see #nanos()
     * @see <a href="https://api.nanos.cc/">https://api.nanos.cc/</a>
     */
    public static RpcQueryNode nanos(String apiKey) {
        return usingRpcProxy(JNH.parseURL(URL_NANOS_CC), apiKey);
    }
    
    /**
     * Creates an {@code RpcQueryNode} object which connects to the <a href="https://powernode.cc/api">powernode.cc</a>
     * API endpoint securely via {@code https}.
     *
     * <p><strong>Note:</strong> this service only permits {@code 5000} free requests each day.</p>
     *
     * <p>Some commands may be unavailable or restricted, and no guarantees on the reliability or security are provided.
     * <strong>You should never send any private keys, seeds or passwords to third-party services.</strong></p>
     *
     * @return the configured RPC node object
     *
     * @see #powerNode(String)
     * @see <a href="https://powernode.cc/api">https://powernode.cc/api</a>
     */
    public static RpcQueryNode powerNode() {
        return powerNode(null);
    }
    
    /**
     * Creates an {@code RpcQueryNode} object which connects to the <a href="https://powernode.cc/api">powernode.cc</a>
     * API endpoint securely via {@code https}.
     *
     * <p><strong>Note:</strong> this service requires an API key to use, and charges for each query.</p>
     *
     * <p>Some commands may be unavailable or restricted, and no guarantees on the reliability or security are provided.
     * <strong>You should never send any private keys, seeds or passwords to third-party services.</strong></p>
     *
     * @param apiKey your API key for authentication
     * @return the configured RPC node object
     *
     * @see #powerNode()
     * @see <a href="https://powernode.cc/api">https://powernode.cc/api</a>
     */
    public static RpcQueryNode powerNode(String apiKey) {
        return usingRpcProxy(JNH.parseURL(URL_POWERNODE), apiKey);
    }
    
    /**
     * Creates an {@code RpcQueryNode} object which connects to the <a href="https://node.somenano.com/#node-rpc">
     * somenano.com</a> API endpoint securely via {@code https}.
     *
     * <p><strong>Note:</strong> this service only permits {@code 5000} free requests each day.</p>
     *
     * <p>Some commands may be unavailable or restricted, and no guarantees on the reliability or security are provided.
     * <strong>You should never send any private keys, seeds or passwords to third-party services.</strong></p>
     *
     * @return the configured RPC node object
     *
     * @see <a href="https://node.somenano.com/#node-rpc">https://node.somenano.com/#node-rpc</a>
     */
    public static RpcQueryNode someNano() {
        return usingRpcProxy(JNH.parseURL(URL_SOMENANO), null);
    }
    
    
    /**
     * Creates an {@code RpcQueryNode} compatible with the <a href="https://github.com/Joohansson/NanoRPCProxy">RPC
     * proxy tool</a>.
     *
     * @param url    the URL of the proxy service
     * @param apiKey the API key ({@code token_key}) used for authentication, or null if not used
     * @return the configured RPC node object
     *
     * @see <a href="https://github.com/Joohansson/NanoRPCProxy">NanoRPCProxy on GitHub</a>
     */
    public static RpcQueryNode usingRpcProxy(URL url, String apiKey) {
        return new RpcQueryNode.Builder(url)
                .setSerializer(new SerializerWithToken("token_key", apiKey))
                .setDeserializer(new JsonResponseDeserializer() {
                    @Override
                    protected JsonObject parseJson(String response) throws RpcException {
                        if (response.equals("You are making requests too fast, please slow down!"))
                            throw new RpcThirdPartyException.TooManyRequestsException(
                                    "Sending requests too fast.", response);
                        if (response.indexOf('{') == -1 && response.contains("Max allowed requests of"))
                            throw new RpcThirdPartyException.TokensExhaustedException(
                                    "Free allowance or paid tokens depleted.", response);
                        return super.parseJson(response);
                    }
                }).build();
    }
    
    
    /** HTTP executor which sends an authentication header */
    private static class ExecutorWithAuth extends HttpRequestExecutor {
        private final String auth;
        public ExecutorWithAuth(URL url, String auth) {
            super(url);
            this.auth = auth;
        }
        
        @Override
        protected void setRequestHeaders(HttpURLConnection con) throws IOException {
            super.setRequestHeaders(con);
            if (auth != null) con.setRequestProperty("authentication", auth);
        }
    }
    
    /** JSON serializer with key/value token in request */
    private static class SerializerWithToken extends JsonRequestSerializer {
        private final String key, value;
        public SerializerWithToken(String key, String value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public JsonObject serializeJsonObject(RpcRequest<?> request) {
            JsonObject json = super.serializeJsonObject(request);
            if (key != null && value != null) json.addProperty(key, value);
            return json;
        }
    }
    
}
