/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc;

import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.rpc.exception.RpcException;
import uk.oczadly.karl.jnano.rpc.exception.RpcUnhandledException;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;
import uk.oczadly.karl.jnano.rpc.util.RpcServiceProviders;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.*;

/**
 * This class is used to connect to an external Nano node endpoint, to which requests and queries can be sent.
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
 * @see RpcServiceProviders
 * @see Builder
 */
public class RpcQueryNode {
    
    private static final int DEFAULT_PORT = 7076;
    private static final RpcRequestSerializer DEFAULT_SERIALIZER = new JsonRequestSerializer();
    private static final RpcResponseDeserializer DEFAULT_DESERIALIZER = new JsonResponseDeserializer();
    private static final ThreadFactory THREAD_FACTORY = JNH.threadFactory("RPC-AsyncExecutor", false);
    
    private final int defaultTimeout;
    private final RpcRequestSerializer requestSerializer;
    private final RpcResponseDeserializer responseDeserializer;
    private final RpcRequestExecutor requestExecutor;
    private final ExecutorService executorService;
    
    
    /**
     * Constructs a new RPC communications object via {@code http} on localhost ({@code ::1}) on port
     * {@value DEFAULT_PORT}.
     */
    public RpcQueryNode() {
        this(DEFAULT_PORT);
    }
    
    /**
     * Constructs a new RPC communications object via {@code http} on localhost ({@code ::1}) on the specified port.
     *
     * @param port the endpoint port number
     *
     * @see Builder
     */
    public RpcQueryNode(int port) {
        this(0, DEFAULT_SERIALIZER, DEFAULT_DESERIALIZER, newLocalhostExecutor(port), newDefaultExecutor());
    }
    
    /**
     * Constructs a new RPC communications object via {@code http} on the specified host address and port.
     *
     * @param address the endpoint address or hostname
     * @param port    the endpoint port number
     *
     * @see Builder
     */
    public RpcQueryNode(String address, int port) {
        this(JNH.parseURL("HTTP", address, port));
    }
    
    /**
     * Constructs a new RPC communications object via the specified protocol, host address and port. The protocol of the
     * URL must be either {@code http} or {@code https}.
     *
     * @param url the endpoint URL of the node (protocol, address and port)
     *
     * @see Builder
     */
    public RpcQueryNode(URL url) {
        this(0, DEFAULT_SERIALIZER, DEFAULT_DESERIALIZER, new HttpRequestExecutor(url), newDefaultExecutor());
    }
    
    private RpcQueryNode(int defaultTimeout, RpcRequestSerializer serializer, RpcResponseDeserializer deserializer,
                         RpcRequestExecutor executor, ExecutorService executorService) {
        if (defaultTimeout < 0)
            throw new IllegalArgumentException("Default timeout value must be positive or zero.");
        if (serializer == null || deserializer == null || executor == null || executorService == null)
            throw new IllegalArgumentException("Arguments cannot be null.");
        
        this.defaultTimeout = defaultTimeout;
        this.requestSerializer = serializer;
        this.responseDeserializer = deserializer;
        this.requestExecutor = executor;
        this.executorService = executorService;
    }
    
    
    /**
     * @return the default timeout period in milliseconds, or {@code 0} if timeouts are disabled
     */
    public final int getDefaultTimeout() {
        return this.defaultTimeout;
    }
    
    /**
     * @return the RpcRequestSerializer which converts request objects into a String format
     */
    public final RpcRequestSerializer getRequestSerializer() {
        return requestSerializer;
    }
    
    /**
     * @return the RpcRequestDeserializer which converts the returned String into a response class
     */
    public final RpcResponseDeserializer getResponseDeserializer() {
        return responseDeserializer;
    }
    
    /**
     * @return the RpcRequestExecutor which submits the request to the external node
     */
    public final RpcRequestExecutor getRequestExecutor() {
        return requestExecutor;
    }
    
    /**
     * @return the ExecutorService used to process asynchronous queries
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
    
        // Serialize the request
        String requestJson = JNH.tryRethrow(
                () -> requestSerializer.serialize(request),
                e -> new RpcUnhandledException("An unhandled error occurred when serializing the request object.", e));
        return processRequestRaw(requestJson, timeout, request.getResponseClass());
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
        return processRequestAsync(request, defaultTimeout, null);
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
        return processRequestAsync(request, timeout, null);
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
            QueryCallback<? super Q, ? super R> callback) {
        return processRequestAsync(request, defaultTimeout, callback);
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
            QueryCallback<? super Q, ? super R> callback) {
        if (request == null)
            throw new IllegalArgumentException("Request argument must not be null.");
        if (timeout < 0)
            throw new IllegalArgumentException("Timeout period must be zero or greater.");
        
        return executorService.submit(new AsyncExecutorTask<>(request, timeout, callback));
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
    
        // Send the request to the node
        String responseData;
        try {
            responseData = processRequestRaw(jsonRequest, timeout);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new RpcUnhandledException("An unhandled error occurred when submitting the request to the node.", e);
        }
        
        // Deserialize response and return
        try {
            return responseDeserializer.deserialize(responseData, responseClass);
        } catch (RpcException e) {
            throw e;
        } catch (Exception e) {
            throw new RpcUnhandledException("An unhandled error occurred when deserializing the response.", e);
        }
    }
    
    /**
     * Sends a raw JSON query to the RPC server, and then returns the raw JSON response.
     *
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
        
        return requestExecutor.submit(jsonRequest, timeout);
    }
    
    
    /**
     * Returns a new builder object for constructing {@code RpcQueryNode} objects.
     * @return a new builder object
     */
    public static Builder builder() {
        return new Builder();
    }
    
    
    private static ExecutorService newDefaultExecutor() {
        return Executors.newFixedThreadPool(250, THREAD_FACTORY);
    }
    
    private static RpcRequestExecutor newLocalhostExecutor(int port) {
        if (port <= 0 || port > 65535)
            throw new IllegalArgumentException("Invalid port number.");
        return new HttpRequestExecutor(JNH.parseURL("HTTP", "::1", port));
    }
    
    
    private class AsyncExecutorTask<Q extends RpcRequest<R>, R extends RpcResponse> implements Callable<R> {
        private final Q request;
        private final QueryCallback<? super Q, ? super R> callback;
        private final int timeout;
        
        public AsyncExecutorTask(Q request, int timeout, QueryCallback<? super Q, ? super R> callback) {
            this.request = request;
            this.timeout = timeout;
            this.callback = callback;
        }
        
        @Override
        public R call() throws Exception {
            // Exceptions are re-thrown so the returned Future object can retrieve them
            try {
                R response = processRequest(request, timeout);
                if (callback != null)
                    callback.onResponse(response, request);
                return response;
            } catch (RpcException ex) {
                if (callback != null)
                    callback.onFailure(ex, request);
                throw ex;
            } catch (IOException ex) {
                if (callback != null)
                    callback.onFailure(ex, request);
                throw ex;
            } catch (Exception ex) { // Shouldn't happen!
                ex.printStackTrace();
                throw ex;
            }
        }
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
     *
     * @see RpcServiceProviders
     */
    public static class Builder {
        private int defaultTimeout = 0;
        private RpcRequestSerializer serializer;
        private RpcResponseDeserializer deserializer;
        private RpcRequestExecutor requestExecutor;
        private ExecutorService executorService;
    
        /**
         * Creates a builder with no pre-defined parameters.
         */
        public Builder() {}
    
        /**
         * Creates a builder using the given endpoint address. The protocol of the URL must be either {@code http} or
         * {@code https}.
         *
         * @param address the endpoint URL of the node (protocol, address and port)
         */
        public Builder(URL address) {
            setAddress(address);
        }
    
        /**
         * Creates a builder copying the defined parameters of the constructed object. Note that this will also use
         * the same {@code ExecutorService} for async queries, unless overwritten by the setter.
         * @param rpc the {@code RpcQueryNode} object to copy
         */
        public Builder(RpcQueryNode rpc) {
            this.defaultTimeout = rpc.defaultTimeout;
            this.serializer = rpc.requestSerializer;
            this.deserializer = rpc.responseDeserializer;
            this.requestExecutor = rpc.requestExecutor;
            this.executorService = rpc.executorService;
        }
    
        
        /**
         * Sets the endpoint address of the node to the given URL. The protocol of the URL must be either {@code http}
         * or {@code https}.
         * 
         * <p>This will overwrite the {@code RpcRequestExecutor} value with a {@link HttpRequestExecutor} configured to
         * the specified address.</p>
         *
         * @param address the endpoint URL of the node (protocol, address and port)
         * @return this builder
         * @see #setRequestExecutor(RpcRequestExecutor)
         */
        public Builder setAddress(URL address) {
            return setRequestExecutor(new HttpRequestExecutor(address));
        }
    
        /**
         * Sets the endpoint address of the node to the given address and port using the {@code http} protocol.
         *
         * <p>This will overwrite the {@code RpcRequestExecutor} value with a {@link HttpRequestExecutor} configured to
         * the specified address.</p>
         *
         * @param address the endpoint address or hostname
         * @param port    the endpoint port number
         * @return this builder
         * @throws MalformedURLException if the address is invalid or the port is out of range
         * @see #setRequestExecutor(RpcRequestExecutor)
         */
        public Builder setAddress(String address, int port) throws MalformedURLException {
            return setAddress(new URL("HTTP", address, port, ""));
        }
    
        /**
         * Sets the {@link RpcRequestExecutor} to connect via {@code http} on localhost ({@code ::1}) on the
         * specified port.
         *
         * <p>This will overwrite the {@code RpcRequestExecutor} value with a {@link HttpRequestExecutor} configured to
         * the specified address.</p>
         *
         * @param port the endpoint port number
         * @return this builder
         * @see #setRequestExecutor(RpcRequestExecutor)
         */
        public Builder setLocalAddress(int port) {
            return setRequestExecutor(newLocalhostExecutor(port));
        }
    
        /**
         * Sets the {@link RpcRequestExecutor} to connect via {@code http} on localhost ({@code ::1}) on port
         * {@value DEFAULT_PORT}.
         *
         * <p>This will overwrite the {@code RpcRequestExecutor} value with a {@link HttpRequestExecutor} configured to
         * the specified address.</p>
         *
         * @return this builder
         * @see #setRequestExecutor(RpcRequestExecutor)
         */
        public Builder setLocalAddress() {
            return setLocalAddress(DEFAULT_PORT);
        }
        
        /**
         * Sets the timeout value to be used for requests which do not specify a timeout value.
         * @param defaultTimeout the timeout value in milliseconds, or zero for indefinite
         * @return this builder
         */
        public Builder setDefaultTimeout(int defaultTimeout) {
            if (defaultTimeout < 0)
                throw new IllegalArgumentException("Timeout must be zero (indefinite) or greater.");
            this.defaultTimeout = defaultTimeout;
            return this;
        }
        
        /**
         * Sets the {@link RpcRequestSerializer} object which converts {@link RpcRequest} instances into JSON strings.
         * @param serializer the serializer object
         * @return this builder
         */
        public Builder setSerializer(RpcRequestSerializer serializer) {
            if (serializer == null)
                throw new IllegalArgumentException("Serializer cannot be null.");
            this.serializer = serializer;
            return this;
        }
        
        /**
         * Sets the {@link RpcResponseDeserializer} object which maps the JSON strings into {@link RpcResponse}
         * objects.
         * @param deserializer the deserializer object
         * @return this builder
         */
        public Builder setDeserializer(RpcResponseDeserializer deserializer) {
            if (deserializer == null)
                throw new IllegalArgumentException("Deserializer cannot be null.");
            this.deserializer = deserializer;
            return this;
        }
        
        /**
         * Sets the {@link RpcRequestExecutor} object which submits requests and reads the response data from the
         * external node.
         * @param requestExecutor the executor object
         * @return this builder
         */
        public Builder setRequestExecutor(RpcRequestExecutor requestExecutor) {
            if (requestExecutor == null)
                throw new IllegalArgumentException("Request executor cannot be null.");
            this.requestExecutor = requestExecutor;
            return this;
        }
        
        /**
         * Sets the {@link ExecutorService} which is used to execute asynchronous queries.
         * @param executorService the ExecutorService used for async queries
         * @return this builder
         */
        public Builder setAsyncExecutorService(ExecutorService executorService) {
            if (executorService == null)
                throw new IllegalArgumentException("Executor service cannot be null.");
            this.executorService = executorService;
            return this;
        }
    
    
        /**
         * Creates a new {@link RpcQueryNode} object from the configured parameters.
         *
         * <p>If no URL, port or RpcRequestExecutor values are set, then the endpoint will be configured to connect
         * via {@code http} on localhost ({@code ::1}) on port {@value DEFAULT_PORT}.</p>
         *
         * @return a new {@link RpcQueryNode} object
         */
        public RpcQueryNode build() {
            return new RpcQueryNode(
                    defaultTimeout,
                    serializer == null ? DEFAULT_SERIALIZER : serializer,
                    deserializer == null ? DEFAULT_DESERIALIZER : deserializer,
                    requestExecutor == null ? newLocalhostExecutor(DEFAULT_PORT) : requestExecutor,
                    executorService != null ? executorService : newDefaultExecutor());
        }
    }
    
}
