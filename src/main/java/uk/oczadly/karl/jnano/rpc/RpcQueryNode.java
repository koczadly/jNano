package uk.oczadly.karl.jnano.rpc;

import uk.oczadly.karl.jnano.rpc.exception.RpcException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <p>This class represents a connection to a specified Nano node endpoint, with the main purpose of sending and
 * queuing RPC requests.</p>
 * <p>To use this class, set the endpoint address, port and authorization token (if configured) in the constructor,
 * and then pass request arguments to one of the {@code processRequest()} methods. Asynchronous requests can also be
 * accomplished using one of the {@code processRequestAsync} methods, which can take a callback, as well as returning a
 * future object representing the response.</p>
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
    
    protected static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    
    private final URL address;
    
    private volatile String authToken;
    private volatile int defaultTimeout;
    
    private final RpcRequestSerializer requestSerializer = new RpcRequestSerializer();
    private final RpcResponseDeserializer responseDeserializer = new RpcResponseDeserializer();
    private final RpcRequestSubmitter requestSubmitter = new RpcRequestSubmitter();
    
    
    /**
     * Constructs a new query node with the address {@code 127.0.0.1:7076}.
     *
     * @throws MalformedURLException if the address cannot be parsed
     */
    public RpcQueryNode() throws MalformedURLException {
        this((String)null);
    }
    
    /**
     * Constructs a new query node with the address {@code 127.0.0.1:7076} and specified authorization token.
     *
     * @param authToken the authorization token to be sent with queries
     * @throws MalformedURLException if the address cannot be parsed
     */
    public RpcQueryNode(String authToken) throws MalformedURLException {
        this("::1", 7076, authToken); // Local address and default port
    }
    
    /**
     * Constructs a new query node with the provided address and port.
     *
     * @param address the address of the node
     * @param port    the port which the node is listening on
     * @throws MalformedURLException if the address cannot be parsed
     */
    public RpcQueryNode(String address, int port) throws MalformedURLException {
        this(address, port, null);
    }
    
    /**
     * Constructs a new query node with the given address, port and authorization token.
     *
     * @param address   the address of the node
     * @param port      the port which the node is listening on
     * @param authToken the authorization token to be sent with queries
     * @throws MalformedURLException if the address cannot be parsed
     */
    public RpcQueryNode(String address, int port, String authToken) throws MalformedURLException {
        this(new URL("HTTP", address, port, ""), authToken);
    }
    
    /**
     * Constructs a new query node with the given address (as a URL).
     *
     * @param address the HTTP URL (address and port) which the node is listening on
     */
    public RpcQueryNode(URL address) {
        this(address, null);
    }
    
    /**
     * Constructs a new query node with the given address (as a URL) and authorization token.
     *
     * @param address   the HTTP URL (address and port) which the node is listening on
     * @param authToken the authorization token to be sent with queries
     */
    public RpcQueryNode(URL address, String authToken) {
        this.address = address;
        this.authToken = authToken;
        this.defaultTimeout = 0;
    }
    
    
    /**
     * @return the address of this node's RPC listener
     */
    public final URL getAddress() {
        return this.address;
    }
    
    
    /**
     * @return the authorization token to be sent to the RPC server, or null if not configured
     */
    public final String getAuthToken() {
        return this.authToken;
    }
    
    /**
     * Sets the authorization token to be used with future requests.
     *
     * @param authToken the new token to be used for queries, or null to remove
     */
    public final void setAuthToken(String authToken) {
        this.authToken = authToken;
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
    public final void setDefaultTimeout(int defaultTimeout) {
        if (defaultTimeout < 0)
            throw new IllegalArgumentException("Default timeout value must be positive, zero or null.");
        
        this.defaultTimeout = defaultTimeout;
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
    public final RpcRequestSubmitter getRequestSubmitter() {
        return requestSubmitter;
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
        return this.processRequest(request, null);
    }
    
    /**
     * Sends a query request to the node via RPC with the specified timeout.
     *
     * @param request the query request to send to the node
     * @param timeout the timeout for the request in milliseconds, {@code 0} for none, or {@code null} for default
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
    public <Q extends RpcRequest<R>, R extends RpcResponse> R processRequest(Q request, Integer timeout)
            throws IOException, RpcException {
        if (request == null)
            throw new IllegalArgumentException("Request argument must not be null.");
        if (timeout != null && timeout < 0)
            throw new IllegalArgumentException("Timeout period must be positive, zero or null.");
        
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
        return this.processRequestAsync(request, null, null);
    }
    
    /**
     * Sends an asynchronous query request to the node via RPC with the specified timeout. This method is non-blocking,
     * and will queue the request up to be processed in a separate worker thread. The returned {@link Future} object
     * should be used to retrieve the status or result of the request at a later time, and will encapsulate any
     * {@link IOException} or {@link RpcException} exceptions thrown during the process.
     *
     * @param request the query request to send to the node
     * @param timeout the timeout for the request in milliseconds, {@code 0} for none, or {@code null} for default
     * @param <Q>     the request type
     * @param <R>     the response type
     * @return a future instance representing the response data/exception
     *
     * @see <a href="https://github.com/koczadly/jNano/wiki/Query-requests#command-lookup-table">See the GitHub wiki
     * for a list of supported request operations.</a>
     */
    public <Q extends RpcRequest<R>, R extends RpcResponse> Future<R> processRequestAsync(Q request, Integer timeout) {
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
        return this.processRequestAsync(request, null, callback);
    }
    
    /**
     * Sends an asynchronous query request to the node via RPC with the specified timeout. This method is non-blocking,
     * and will queue the request up to be processed in a separate worker thread. In conjunction with the
     * receiving callback instance, the returned {@link Future} object may be used to retrieve the status or result of
     * the request at a later time, and will encapsulate any {@link IOException} or {@link RpcException} exceptions
     * thrown during the process.
     *
     * @param request  the query request to send to the node
     * @param timeout  the timeout for the request in milliseconds, {@code 0} for none, or {@code null} for default
     * @param callback the callback to execute after the request has completed (or null for no callback)
     * @param <Q>      the request type
     * @param <R>      the response type
     * @return a future instance representing the response data/exception
     *
     * @see <a href="https://github.com/koczadly/jNano/wiki/Query-requests#command-lookup-table">See the GitHub wiki
     * for a list of supported request operations.</a>
     */
    public <Q extends RpcRequest<R>, R extends RpcResponse> Future<R> processRequestAsync(Q request, Integer timeout,
            QueryCallback<Q, R> callback) {
        if (request == null)
            throw new IllegalArgumentException("Request argument must not be null.");
        if (timeout != null && timeout < 0)
            throw new IllegalArgumentException("Timeout period must be positive, zero or null.");
        
        return RpcQueryNode.EXECUTOR_SERVICE.submit(() -> {
            try {
                R response = RpcQueryNode.this.processRequest(request, timeout);
                if (callback != null)
                    callback.onResponse(response, request);
                return response; // Return for Future object
            } catch (RpcException ex) {
                if (callback != null)
                    callback.onRpcFailure(ex, request);
                throw ex; // Re-throw for Future object
            } catch (Exception ex) {
                if (callback != null)
                    callback.onFailure(ex, request);
                throw ex; // Re-throw for Future object
            }
        });
    }
    
    
    /**
     * Sends a raw JSON query to the RPC server, and then returns an object in the specified class containing the
     * deserialized response data.
     *
     * @param jsonRequest   the JSON query to send to the node
     * @param timeout       the connection timeout in milliseconds, or null to disable timeouts
     * @param responseClass the class to deserialize the response data into
     * @param <R>           the response type
     * @return the response received from the node, contained in an object of the specified class
     *
     * @throws IOException  if an error occurs with the connection to the node
     * @throws RpcException if the node returns a non-successful response
     */
    public <R extends RpcResponse> R processRequestRaw(String jsonRequest, Integer timeout, Class<R> responseClass)
            throws IOException, RpcException {
        if (responseClass == null)
            throw new IllegalArgumentException("Response class argument cannot be null.");
        
        String responseJson = this.processRequestRaw(jsonRequest, timeout); // Send the request to the node
        return this.responseDeserializer.deserialize(responseJson, responseClass);
    }
    
    /**
     * <p>Sends a raw JSON query to the RPC server, and then returns the raw JSON response.</p>
     * <p>Note that this method will not deserialize the resulting JSON, or parse it for errors reported by the node.
     * You will need to implement this functionality yourself, or use the alternate {@link #processRequestRaw(String,
     * Integer, Class)} method.</p>
     *
     * @param jsonRequest the JSON query to send to the node
     * @param timeout     the connection timeout in milliseconds, or null to disable timeouts
     * @return the JSON response received from the node
     *
     * @throws IOException if an error occurs with the connection to the node
     */
    public String processRequestRaw(String jsonRequest, Integer timeout) throws IOException {
        if (timeout == null) {
            timeout = 0;
        } else if (timeout < 0) {
            throw new IllegalArgumentException("Timeout period must be positive, zero or null.");
        }
        
        return requestSubmitter.submit(address, authToken, jsonRequest, timeout);
    }
    
}
