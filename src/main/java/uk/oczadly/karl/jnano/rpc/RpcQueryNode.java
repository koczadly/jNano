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
 * <p>This class represents a connection to a specified Nano node endpoint, with the main purpose of sending and
 * queuing RPC requests.</p>
 * <p>To use this class, set the endpoint address and port in the constructor, and then pass request arguments to one of
 * the {@code processRequest()} methods. Asynchronous requests can also be accomplished using one of the
 * {@code processRequestAsync} methods, which can take a callback, as well as returning a future object representing the
 * response.</p>
 * <p>Below is an example of a synchronous query which creates a new account from a provided wallet ID:</p>
 * <pre>
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
 *      // Output new account
 *      System.out.println("New account: " + response.getAccountAddress());
 *  } catch (RpcException | IOException e) {
 *      e.printStackTrace();
 *  }
 * </pre>
 */
public class RpcQueryNode {
    
    private final URL address;
    
    private volatile int defaultTimeout = 0;
    private volatile RpcRequestSerializer requestSerializer = new RpcRequestSerializerImpl();
    private volatile RpcResponseDeserializer responseDeserializer = new RpcResponseDeserializerImpl();
    private volatile RpcRequestSubmitter requestSubmitter = new RpcRequestSubmitterImpl();
    private volatile ExecutorService executorService = Executors.newFixedThreadPool(100);
    
    
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
     */
    public RpcQueryNode(int port) {
        this(JNH.unchecked(() -> new URL("HTTP", "::1", port, "")));
    }
    
    /**
     * Constructs a new query node with the given address and port number.
     *
     * @param address   the address of the node
     * @param port      the port which the node is listening on
     * @throws MalformedURLException if the address cannot be parsed
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
     */
    public RpcQueryNode(URL address) {
        if (address == null)
            throw new IllegalArgumentException("Address argument cannot be null.");
        this.address = address;
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
     * Sets the default timeout period when unspecified for requests. Setting this value will not affect queries
     * already being processed, and will only apply to new queries.
     *
     * @param defaultTimeout the timeout period in milliseconds, or {@code 0} to disable timeouts
     */
    public final RpcQueryNode setDefaultTimeout(int defaultTimeout) {
        if (defaultTimeout < 0)
            throw new IllegalArgumentException("Default timeout value must be positive, zero or null.");
        
        this.defaultTimeout = defaultTimeout;
        return this;
    }
    
    /**
     * @return the object responsible for serializing requests
     */
    public final RpcRequestSerializer getRequestSerializer() {
        return requestSerializer;
    }
    
    /**
     * Sets the serializer object used to convert request instances into string form, which is then sent to the node
     * for processing.
     * @param requestSerializer the serializer object
     * @return this instance
     */
    public final RpcQueryNode setRequestSerializer(RpcRequestSerializer requestSerializer) {
        if (requestSerializer == null)
            throw new IllegalArgumentException("RpcRequestSerializer argument cannot be null.");
        
        this.requestSerializer = requestSerializer;
        return this;
    }
    
    /**
     * @return the object responsible for deserializing and parsing errors of responses
     */
    public final RpcResponseDeserializer getResponseDeserializer() {
        return responseDeserializer;
    }
    
    /**
     * Sets the deserializer object used to convert the string response into a response class.
     * @param responseDeserializer the deserializer object
     * @return this instance
     */
    public final RpcQueryNode setResponseDeserializer(RpcResponseDeserializer responseDeserializer) {
        if (responseDeserializer == null)
            throw new IllegalArgumentException("RpcResponseDeserializer argument cannot be null.");
        
        this.responseDeserializer = responseDeserializer;
        return this;
    }
    
    /**
     * @return the object responsible for submitting requests to the node
     */
    public final RpcRequestSubmitter getRequestSubmitter() {
        return requestSubmitter;
    }
    
    /**
     * Sets the submitter object used to submit requests to the remote node.
     * @param requestSubmitter the submitter object
     * @return this instance
     */
    public final RpcQueryNode setRequestSubmitter(RpcRequestSubmitter requestSubmitter) {
        if (requestSubmitter == null)
            throw new IllegalArgumentException("RpcRequestSubmitter argument cannot be null.");
        
        this.requestSubmitter = requestSubmitter;
        return this;
    }
    
    /**
     * @return the executor service used to process asynchronous queries
     */
    public final ExecutorService getExecutorService() {
        return executorService;
    }
    
    /**
     * Sets the executor service used to process asynchronous queries.
     * @param executorService the executor service
     * @return this instance
     */
    public final RpcQueryNode setExecutorService(ExecutorService executorService) {
        if (executorService == null)
            throw new IllegalArgumentException("ExecutorService argument cannot be null.");
        
        this.executorService = executorService;
        return this;
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
        if (timeout < 0)
            throw new IllegalArgumentException("Timeout period must be zero or greater.");
        
        return requestSubmitter.submit(address, jsonRequest, timeout);
    }
    
}
