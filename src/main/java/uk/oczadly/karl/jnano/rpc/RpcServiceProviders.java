/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc;

import com.google.gson.JsonObject;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class contains a set of static initializers for publicly available third-party RPC endpoints.
 */
public class RpcServiceProviders {
    
    private static final String URL_NANEX_CC = "https://api.nanex.cc";
    private static final String URL_NINJA    = "https://mynano.ninja/api/node";
    private static final String URL_NANOS_CC = "https://proxy.nanos.cc/proxy";
    
    
    /**
     * Creates an {@code RpcQueryNode} object which connects to the <a href="https://nanex.cc">nanex.cc</a> API
     * endpoint securely via {@code https}.
     *
     * <p>Some commands may be unavailable or restricted, and no guarantees on the reliability or security are provided.
     * You should never send any private keys, seeds or passwords to remote endpoints.</p>
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
     * <p><strong>Note:</strong> This service only permits {@code 500} free requests each hour.</p>
     *
     * <p>Some commands may be unavailable or restricted, and no guarantees on the reliability or security are provided.
     * You should never send any private keys, seeds or passwords to remote endpoints.</p>
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
     * <p><strong>Note:</strong> This service requires an API key to use, and charges for each query.</p>
     *
     * <p>Some commands may be unavailable or restricted, and no guarantees on the reliability or security are provided.
     * You should never send any private keys, seeds or passwords to remote endpoints.</p>
     *
     * @param apiKey your API key for authorization
     * @return the configured RPC node object
     *
     * @see #myNanoNinja() 
     * @see <a href="https://mynano.ninja/api/node">https://mynano.ninja/api/node</a>
     */
    public static RpcQueryNode myNanoNinja(String apiKey) {
        return new RpcQueryNode.Builder()
                .setRequestExecutor(new HttpRequestExecutorWithAuth(JNH.parseURL(URL_NINJA), apiKey))
                .build();
    }
    
    /**
     * Creates an {@code RpcQueryNode} object which connects to the <a href="https://api.nanos.cc/">nanos.cc</a> API
     * endpoint securely via {@code https}.
     *
     * <p><strong>Note:</strong> This service only permits {@code 5000} free requests each day.</p>
     *
     * <p>Some commands may be unavailable or restricted, and no guarantees on the reliability or security are provided.
     * You should never send any private keys, seeds or passwords to remote endpoints.</p>
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
     * <p><strong>Note:</strong> This service requires an API key to use, and charges for each query.</p>
     *
     * <p>Some commands may be unavailable or restricted, and no guarantees on the reliability or security are provided.
     * You should never send any private keys, seeds or passwords to remote endpoints.</p>
     *
     * @param apiKey your API key for authorization
     * @return the configured RPC node object
     *
     * @see #nanos()
     * @see <a href="https://api.nanos.cc/">https://api.nanos.cc/</a>
     */
    public static RpcQueryNode nanos(String apiKey) {
        return new RpcQueryNode.Builder(JNH.parseURL(URL_NANOS_CC))
                .setSerializer(new JsonRequestSerializerWithToken("token_key", apiKey))
                .build();
    }
    
    
    
    /** HTTP executor which sends an Authorization header */
    private static class HttpRequestExecutorWithAuth extends HttpRequestExecutor {
        private final String auth;
        
        public HttpRequestExecutorWithAuth(URL url, String auth) {
            super(url);
            this.auth = auth;
        }
        
        @Override
        protected void setRequestHeaders(HttpURLConnection con) throws IOException {
            super.setRequestHeaders(con);
            if (auth != null)
                con.setRequestProperty("Authorization", auth);
        }
    }
    
    /** JSON serializer with key/value token in request */
    private static class JsonRequestSerializerWithToken extends JsonRequestSerializer {
        private final String key, value;
        
        public JsonRequestSerializerWithToken(String key, String value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public JsonObject serializeJsonObject(RpcRequest<?> request) {
            JsonObject json = super.serializeJsonObject(request);
            if (key != null && value != null)
                json.addProperty(key, value);
            return json;
        }
    }

}
