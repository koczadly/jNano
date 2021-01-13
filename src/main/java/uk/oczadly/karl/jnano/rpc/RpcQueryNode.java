/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc;

import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.rpc.exception.RpcException;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <p>This class represents a connection to a specified Nano node endpoint, with the main intent of sending and
 * queuing RPC requests.</p>
 *
 * <p>To use this class, set the endpoint address and port in the constructor, or use the provided {@link Builder} class
 * for additional configuration parameters). To make a request, first create a request object that inherits from
 * {@link RpcRequest}, specifying any parameters within the request's constructor. Now pass this request object to one
 * of the {@code processRequest} methods within this class, and use the returned {@link RpcResponse} object to access
 * the response data.</p>
 *
 * <p>The asynchronous processing methods ({@code processRequestAsync}) support the options to take a callback object
 * ({@link QueryCallback}) as a parameter, or to return a {@link Future} object. Asynchronous requests will be
 * processed on threads issued by the {@link ExecutorService} specified within this class.</p>
 *
 * <p>Below is an example of a synchronous query which creates a new account from a provided wallet ID:</p>
 * <pre>{@code
 *  try {
 *      // Configure a connection to localhost:7076
 *      RpcQueryNode node = new RpcQueryNode();
 *
 *      // Construct the request (and pass query arguments)
 *      RequestAccountCreate request = new RequestAccountCreate(
 *              "B4ECF585D887B590907949C41F73BB11AA0BD4FD98563CC5D810EF26FAAD948E"); // Wallet ID
 *
 *      // Send request to the node synchronously and retrieve response
 *      ResponseAccount response = node.processRequest(request);
 *
 *      // Output new account to console
 *      System.out.println("New account: " + response.getAccountAddress());
 *  } catch (RpcException | IOException e) {
 *      e.printStackTrace();
 *  }
 * }</pre>
 *
 * @see Builder
 */
public class RpcQueryNode {
    
    private final URL address;
    private final int defaultTimeout;
    private final RpcRequestSerializer requestSerializer;
    private final RpcResponseDeserializer responseDeserializer;
    private final RpcRequestExecutor requestExecutor;
    private final ExecutorService executorService;
    
    
    /**
     * Constructs a new query node with the local loopback address with port 7076 ({@code [::1]:7076}).
     */
    public RpcQueryNode() {
        this(7076);
    }
    
    /**
     * Constructs a new query node with the local loopback address {@code [::1]} and the specified port.
     *
     * @param port the port which the node is listening on
     *
     * @see Builder
     */
    public RpcQueryNode(int port) {
        this(JNH.unchecked(() -> new URL("HTTP", "::1", port, "")));
    }
    
    /**
     * Constructs a new query node with the given address and port number.
     *
     * @param address   the address of the node
     * @param port      the port which the node is listening on
     *
     * @throws MalformedURLException if the address cannot be parsed
     *
     * @see Builder
     */
    public RpcQueryNode(String address, int port) throws MalformedURLException {
        this(new URL("HTTP", address, port, ""));
        
        if (address == null)
            throw new IllegalArgumentException("Address argument cannot be null.");
    }
    
    /**
     * Constructs a new query node with the given address (as a URL).
     *
     * @param address   the HTTP URL (address and port) which the node is listening on
     *
     * @see Builder
     */
    public RpcQueryNode(URL address) {
        this(address, 0, null, null, null, null);
    }
    
    private RpcQueryNode(URL address, int defaultTimeout, RpcRequestSerializer serializer,
                         RpcResponseDeserializer deserializer, RpcRequestExecutor executor,
                         ExecutorService executorService) {
        if (address == null)
            throw new IllegalArgumentException("Address argument cannot be null.");
        if (defaultTimeout < 0)
            throw new IllegalArgumentException("Default timeout value must be positive or zero.");
        
        this.address = address;
        this.defaultTimeout = defaultTimeout;
        this.requestSerializer = serializer != null ? serializer : new JsonRequestSerializer();
        this.responseDeserializer = deserializer != null ? deserializer : new JsonResponseDeserializer();
        this.requestExecutor = executor != null ? executor : new HttpRequestExecutor();
        this.executorService = executorService != null ? executorService : Executors.newFixedThreadPool(250);
    }
    
    
    /**
     * @return the address of this node's RPC listener
     */
    public final URL getAddress() {
        return this.address;
    }
    
    
    /**
     * @return the default timeout period in milliseconds, or {@code 0} if timeouts are disabled
     */
    public final int getDefaultTimeout() {
        return this.defaultTimeout;
    }
    
    /**
     * @return the object responsible for serializing requests
     */
    public final RpcRequestSerializer getRequestSerializer() {
        return requestSerializer;
    }
    
    /**
     * @return the object responsible for deserializing and parsing errors of responses
     */
    public final RpcResponseDeserializer getResponseDeserializer() {
        return responseDeserializer;
    }
    
    /**
     * @return the object responsible for submitting requests to the node
     */
    public final RpcRequestExecutor getRequestExecutor() {
        return requestExecutor;
    }
    
    /**
     * @return the executor service used to process asynchronous queries
     */
    public final ExecutorService getExecutorService() {
        return executorService;
    }
    
    
    /**
     * Sends a query request to the node via RPC with the default timeout.
     *
     * @param request the query request to send to the node
     * @param <Q>     the request type
     * @param <R>     the response type
     * @return the successful reponse from the node
     *
     * @throws IOException  if an error occurs with the connection to the node
     * @throws RpcException if the node returns a non-successful response
     *
     * @see <a href="https://github.com/koczadly/jNano/wiki/Query-requests#command-lookup-table">See the GitHub wiki
     * for a list of supported request operations.</a>
     */
    public <Q extends RpcRequest<R>, R extends RpcResponse> R processRequest(Q request)
            throws IOException, RpcException {
        return this.processRequest(request, defaultTimeout);
    }
    
    /**
     * Sends a query request to the node via RPC with the specified timeout.
     *
     * @param request the query request to send to the node
     * @param timeout the timeout for the request in milliseconds, or zero for infinite
     * @param <Q>     the request type
     * @param <R>     the response type
     * @return the successful reponse from the node
     *
     * @throws IOException  if an error occurs with the connection to the node
     * @throws RpcException if the node returns a non-successful response
     *
     * @see <a href="https://github.com/koczadly/jNano/wiki/Query-requests#command-lookup-table">See the GitHub wiki
     * for a list of supported request operations.</a>
     */
    public <Q extends RpcRequest<R>, R extends RpcResponse> R processRequest(Q request, int timeout)
            throws IOException, RpcException {
        if (request == null)
            throw new IllegalArgumentException("Request argument must not be null.");
        if (timeout < 0)
            throw new IllegalArgumentException("Timeout period must be zero or greater.");
        
        String requestJsonStr = this.requestSerializer.serialize(request); // Serialise the request into JSON
        return this.processRequestRaw(requestJsonStr, timeout, request.getResponseClass());
    }
    
    
    /**
     * Sends an asynchronous query request to the node via RPC with the default timeout. This method is non-blocking,
     * and will queue the request up to be processed in a separate worker thread. The returned {@link Future} object
     * should be used to retrieve the status or result of the request at a later time, and will encapsulate any
     * {@link IOException} or {@link RpcException} exceptions thrown during the process.
     *
     * @param request the query request to send to the node
     * @param <Q>     the request type
     * @param <R>     the response type
     * @return a future instance representing the response data/exception
     *
     * @see <a href="https://github.com/koczadly/jNano/wiki/Query-requests#command-lookup-table">See the GitHub wiki
     * for a list of supported request operations.</a>
     */
    public <Q extends RpcRequest<R>, R extends RpcResponse> Future<R> processRequestAsync(Q request) {
        return this.processRequestAsync(request, defaultTimeout, null);
    }
    
    /**
     * Sends an asynchronous query request to the node via RPC with the specified timeout. This method is non-blocking,
     * and will queue the request up to be processed in a separate worker thread. The returned {@link Future} object
     * should be used to retrieve the status or result of the request at a later time, and will encapsulate any
     * {@link IOException} or {@link RpcException} exceptions thrown during the process.
     *
     * @param request the query request to send to the node
     * @param timeout the timeout for the request in milliseconds, or zero for infinite
     * @param <Q>     the request type
     * @param <R>     the response type
     * @return a future instance representing the response data/exception
     *
     * @see <a href="https://github.com/koczadly/jNano/wiki/Query-requests#command-lookup-table">See the GitHub wiki
     * for a list of supported request operations.</a>
     */
    public <Q extends RpcRequest<R>, R extends RpcResponse> Future<R> processRequestAsync(Q request, int timeout) {
        return this.processRequestAsync(request, timeout, null);
    }
    
    
    /**
     * Sends an asynchronous query request to the node via RPC with the default timeout. This method is non-blocking,
     * and will queue the request up to be processed in a separate worker thread. In conjunction with the
     * receiving callback instance, the returned {@link Future} object may be used to retrieve the status or result of
     * the request at a later time, and will encapsulate any {@link IOException} or {@link RpcException} exceptions
     * thrown during the process.
     *
     * @param request  the query request to send to the node
     * @param callback the callback to execute after the request has completed (or null for no callback)
     * @param <Q>      the request type
     * @param <R>      the response type
     * @return a future instance representing the response data/exception
     *
     * @see <a href="https://github.com/koczadly/jNano/wiki/Query-requests#command-lookup-table">See the GitHub wiki
     * for a list of supported request operations.</a>
     */
    public <Q extends RpcRequest<R>, R extends RpcResponse> Future<R> processRequestAsync(Q request,
            QueryCallback<Q, R> callback) {
        return this.processRequestAsync(request, defaultTimeout, callback);
    }
    
    /**
     * Sends an asynchronous query request to the node via RPC with the specified timeout. This method is non-blocking,
     * and will queue the request up to be processed in a separate worker thread. In conjunction with the
     * receiving callback instance, the returned {@link Future} object may be used to retrieve the status or result of
     * the request at a later time, and will encapsulate any {@link IOException} or {@link RpcException} exceptions
     * thrown during the process.
     *
     * @param request  the query request to send to the node
     * @param timeout  the timeout for the request in milliseconds, or zero for infinite
     * @param callback the callback to execute after the request has completed (or null for no callback)
     * @param <Q>      the request type
     * @param <R>      the response type
     * @return a future instance representing the response data/exception
     *
     * @see <a href="https://github.com/koczadly/jNano/wiki/Query-requests#command-lookup-table">See the GitHub wiki
     * for a list of supported request operations.</a>
     */
    public <Q extends RpcRequest<R>, R extends RpcResponse> Future<R> processRequestAsync(Q request, int timeout,
            QueryCallback<Q, R> callback) {
        if (request == null)
            throw new IllegalArgumentException("Request argument must not be null.");
        if (timeout < 0)
            throw new IllegalArgumentException("Timeout period must be zero or greater.");
        
        return this.executorService.submit(() -> {
            try {
                R response = RpcQueryNode.this.processRequest(request, timeout);
                if (callback != null)
                    callback.onResponse(response, request);
                return response; // Return for Future object
            } catch (RpcException ex) {
                if (callback != null)
                    callback.onFailure(ex, request);
                throw ex; // Re-throw for Future object
            } catch (IOException ex) {
                if (callback != null)
                    callback.onFailure(ex, request);
                throw ex; // Re-throw for Future object
            } catch (Exception ex) { // Shouldn't happen!
                ex.printStackTrace();
                throw ex;
            }
        });
    }
    
    
    /**
     * Sends a raw JSON query to the RPC server, and then returns an object in the specified class containing the
     * deserialized response data.
     *
     * @param jsonRequest   the JSON query to send to the node
     * @param timeout       the timeout for the request in milliseconds, or zero for infinite
     * @param responseClass the class to deserialize the response data into
     * @param <R>           the response type
     * @return the response received from the node, contained in an object of the specified class
     *
     * @throws IOException  if an error occurs with the connection to the node
     * @throws RpcException if the node returns a non-successful response
     */
    public <R extends RpcResponse> R processRequestRaw(String jsonRequest, int timeout, Class<R> responseClass)
            throws IOException, RpcException {
        if (jsonRequest == null)
            throw new IllegalArgumentException("JSON request cannot be null.");
        if (responseClass == null)
            throw new IllegalArgumentException("Response class argument cannot be null.");
        
        try {
            String responseJson = this.processRequestRaw(jsonRequest, timeout); // Send the request to the node
            return this.responseDeserializer.deserialize(responseJson, responseClass);
        } catch (IOException | RpcException e) {
            throw e;
        } catch (Exception e) {
            throw new RpcException("An unhandled exception occured.", e);
        }
    }
    
    /**
     * <p>Sends a raw JSON query to the RPC server, and then returns the raw JSON response.</p>
     * <p>Note that this method will not deserialize the resulting JSON, or parse it for errors reported by the node.
     * You will need to implement this functionality yourself, or use the alternate {@link #processRequestRaw(String,
     * int, Class)} method.</p>
     *
     * @param jsonRequest the JSON query to send to the node
     * @param timeout     the timeout for the request in milliseconds, or zero for infinite
     * @return the JSON response received from the node
     *
     * @throws IOException if an error occurs with the connection to the node
     */
    public String processRequestRaw(String jsonRequest, int timeout) throws IOException {
        if (jsonRequest == null)
            throw new IllegalArgumentException("JSON request cannot be null.");
        if (timeout < 0)
            throw new IllegalArgumentException("Timeout period must be zero or greater.");
        
        return requestExecutor.submit(address, jsonRequest, timeout);
    }
    
    
    /**
     * This builder class allows you to customize and create new instances of the {@link RpcQueryNode} class.
     *
     * <p>The default assigned values are as follows:</p>
     * <table summary="Default values">
     *     <thead><tr><th>Parameter</th><th>Default value</th></tr></thead>
     *     <tbody>
     *         <tr><td>Timeout</td>                 <td>Indefinite ({@code 0})</td></tr>
     *         <tr><td>Request serializer</td>      <td>{@link JsonRequestSerializer}</td></tr>
     *         <tr><td>Response deserializer</td>   <td>{@link JsonResponseDeserializer}</td></tr>
     *         <tr><td>Request executor</td>        <td>{@link HttpRequestExecutor}</td></tr>
     *         <tr><td>Thread executor service</td> <td>{@link Executors#newFixedThreadPool(int)} with up to 250
     *         threads</td></tr>
     *     </tbody>
     * </table>
     */
    public static class Builder {
        private URL address;
        private int defaultTimeout = 0;
        private RpcRequestSerializer serializer;
        private RpcResponseDeserializer deserializer;
        private RpcRequestExecutor requestExecutor;
        private ExecutorService executorService;
    
    
        /**
         * Creates a builder using {@code localhost:7076} as the endpoint address.
         */
        public Builder() {
            JNH.unchecked(() -> setAddress(7076));
        }
    
        /**
         * Creates a builder using the given endpoint address.
         * @param address the URL of the node
         */
        public Builder(URL address) {
            this.address = address;
        }
    
    
        /**
         * @return the URL endpoint of the node
         */
        public URL getAddress() {
            return address;
        }
    
        /**
         * Sets the endpoint address of the node to the given URL.
         * @param address the URL of the node
         * @return this builder
         */
        public Builder setAddress(URL address) {
            this.address = address;
            return this;
        }
    
        /**
         * Sets the endpoint address of the node to the given address and port.
         * @param address the hostname address of the node
         * @param port the port address of the node
         * @return this builder
         * @throws MalformedURLException if the address is invalid or the port is out of range
         */
        public Builder setAddress(String address, int port) throws MalformedURLException {
            return setAddress(new URL("HTTP", address, port, ""));
        }
    
        /**
         * Sets the endpoint address of the node to the given port on {@code localhost}.
         * @param port the port address of the node
         * @return this builder
         * @throws MalformedURLException if the port is out of range
         */
        public Builder setAddress(int port) throws MalformedURLException {
            return setAddress(new URL("HTTP", "::1", port, ""));
        }
    
        /**
         * @return the default timeout value in milliseconds, or zero for indefinite
         */
        public int getDefaultTimeout() {
            return defaultTimeout;
        }
    
        /**
         * Sets the timeout value to be used for requests which do not specify a timeout value.
         * @param defaultTimeout the timeout value in milliseconds, or zero for indefinite
         * @return this builder
         */
        public Builder setDefaultTimeout(int defaultTimeout) {
            this.defaultTimeout = defaultTimeout;
            return this;
        }
    
        /**
         * @return the object which converts {@link RpcRequest} instances into JSON strings
         */
        public RpcRequestSerializer getSerializer() {
            return serializer;
        }
    
        /**
         * Sets the {@link RpcRequestSerializer} object which converts {@link RpcRequest} instances into JSON strings.
         * @param serializer the serializer object
         * @return this builder
         */
        public Builder setSerializer(RpcRequestSerializer serializer) {
            this.serializer = serializer;
            return this;
        }
    
        /**
         * @return the object which maps the returned JSON strings into {@link RpcResponse} objects
         */
        public RpcResponseDeserializer getDeserializer() {
            return deserializer;
        }
    
        /**
         * Sets the {@link RpcResponseDeserializer} object which maps the JSON strings into {@link RpcResponse}
         * objects.
         * @param deserializer the deserializer object
         * @return this builder
         */
        public Builder setDeserializer(RpcResponseDeserializer deserializer) {
            this.deserializer = deserializer;
            return this;
        }
    
        /**
         * @return the object which submits requests to the external node
         */
        public RpcRequestExecutor getRequestExecutor() {
            return requestExecutor;
        }
    
        /**
         * Sets the {@link RpcRequestExecutor} object which submits requests and reads the response data from the
         * external node.
         * @param requestExecutor the executor object
         * @return this builder
         */
        public Builder setRequestExecutor(RpcRequestExecutor requestExecutor) {
            this.requestExecutor = requestExecutor;
            return this;
        }
    
        /**
         * @return the {@link ExecutorService} used for asynchronous execution of queries
         */
        public ExecutorService getAsyncExecutorService() {
            return executorService;
        }
    
        /**
         * Sets the {@link ExecutorService} which is used to execute asynchronous queries.
         * @param executorService the ExecutorService used for async queries
         * @return this builder
         */
        public Builder setAsyncExecutorService(ExecutorService executorService) {
            this.executorService = executorService;
            return this;
        }
    
    
        /**
         * Creates a new {@link RpcQueryNode} object from the configured parameters.
         * @return a new {@link RpcQueryNode} object
         */
        public RpcQueryNode build() {
            return new RpcQueryNode(address, defaultTimeout, serializer, deserializer, requestExecutor,
                    executorService);
        }
    }
    
}
