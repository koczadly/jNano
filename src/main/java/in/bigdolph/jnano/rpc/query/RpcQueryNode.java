package in.bigdolph.jnano.rpc.query;

import com.google.gson.*;
import in.bigdolph.jnano.rpc.adapters.BooleanTypeDeserializer;
import in.bigdolph.jnano.rpc.adapters.hotfix.ArrayTypeAdapterFactory;
import in.bigdolph.jnano.rpc.adapters.hotfix.CollectionTypeAdapterFactory;
import in.bigdolph.jnano.rpc.adapters.hotfix.MapTypeAdapterFactory;
import in.bigdolph.jnano.rpc.query.exception.*;
import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.RpcResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RpcQueryNode {
    
    protected static final JsonParser JSON_PARSER = new JsonParser();
    
    protected static final ExecutorService executorService = Executors.newCachedThreadPool();
    
    private final URL address;
    private final Gson gson;
    private volatile String authToken;
    
    
    /**
     * Constructs a new query node with the address 127.0.0.1:7076
     *
     * @throws MalformedURLException if the address cannot be parsed
     */
    public RpcQueryNode() throws MalformedURLException {
        this((String)null);
    }
    
    /**
     * Constructs a new query node with the address 127.0.0.1:7076
     *
     * @param authToken   the authorization token to be sent with queries
     * @throws MalformedURLException if the address cannot be parsed
     */
    public RpcQueryNode(String authToken) throws MalformedURLException {
        this("127.0.0.1", 7076, authToken); //Local address and default port
    }
    
    /**
     * Constructs a new query node with the given address and port
     *
     * @param address   the address of the node
     * @param port      the port which the node is listening on
     * @throws MalformedURLException if the address cannot be parsed
     */
    public RpcQueryNode(String address, int port) throws MalformedURLException {
        this(address, port, null);
    }
    
    /**
     * Constructs a new query node with the given address and port
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
     * Constructs a new query node with the given address and port
     * @param address   the HTTP URL (address and port) which the node is listening on
     */
    public RpcQueryNode(URL address) {
        this(address, null);
    }
    
    /**
     * Constructs a new query node with the given address and port
     * @param address   the HTTP URL (address and port) which the node is listening on
     * @param authToken the authorization token to be sent with queries
     */
    public RpcQueryNode(URL address, String authToken) {
        this(address, authToken, new GsonBuilder());
    }
    
    protected RpcQueryNode(URL address, String authToken, GsonBuilder gson) {
        this.address = address;
        this.authToken = authToken;
        this.gson = gson.excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapterFactory(new ArrayTypeAdapterFactory())          //Empty array hotfix
                .registerTypeAdapterFactory(new CollectionTypeAdapterFactory())     //Empty collection hotfix
                .registerTypeAdapterFactory(new MapTypeAdapterFactory())            //Empty map hotfix
                .registerTypeAdapter(boolean.class, new BooleanTypeDeserializer())  //Boolean deserializer
                .registerTypeAdapter(Boolean.class, new BooleanTypeDeserializer())  //Boolean deserializer
                .create();
    }
    
    
    
    /**
     * @return the address of this node's RPC listener
     */
    public final URL getAddress() {
        return this.address;
    }
    
    
    /**
     * @return the authorization token to be sent to the RPC server
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
     * @return the Gson utility class used by this instance
     */
    public final Gson getGsonInstance() {
        return this.gson;
    }
    
    
    
    /**
     * Sends a query request to the node via RPC.
     * This method will not timeout as long as the connection remains open.
     *
     * @param request the query request to send to the node
     * @return the successful reponse from the node
     * @throws IOException         if an error occurs with the connection to the node
     * @throws RpcException   if the node returns a non-successful response
     */
    public <Q extends RpcRequest<R>, R extends RpcResponse> R processRequest(Q request) throws IOException, RpcException {
        return this.processRequest(request, null);
    }
    
    /**
     * Sends a query request to the node via RPC.
     *
     * @param request the query request to send to the node
     * @param timeout the timeout for the request in milliseconds, or null for none
     * @return the successful reponse from the node
     * @throws IOException         if an error occurs with the connection to the node
     * @throws RpcException   if the node returns a non-successful response
     */
    public <Q extends RpcRequest<R>, R extends RpcResponse> R processRequest(Q request, Integer timeout) throws IOException, RpcException {
        if(request == null) throw new IllegalArgumentException("Request argument must not be null");
        if(timeout != null && timeout < 0) throw new IllegalArgumentException("Timeout period must be positive or null");
        
        String requestJsonStr = this.serializeRequestToJSON(request); //Serialise the request into JSON
        
        //Configure connection
        HttpURLConnection connection = (HttpURLConnection)this.address.openConnection();
        if(timeout != null) { //Set timeouts
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
        }
        
        String responseJson = this.processRawRequest(requestJsonStr, connection); //Send the request to the node
        
        R response = this.deserializeResponseFromJSON(responseJson, request.getResponseClass());
        assert response != null : "Response JSON is null";
        
        return response;
    }
    
    
    /**
     * Sends a query request to the node via RPC.
     * The request will not timeout as long as the connection remains open.
     *
     * @param request   the query request to send to the node
     * @param callback  the callback to execute after the request has completed
     */
    public <Q extends RpcRequest<R>, R extends RpcResponse> Future<R> processRequestAsync(Q request, QueryCallback<R> callback) {
        return this.processRequestAsync(request, null, callback);
    }
    
    /**
     * Sends a query request to the node via RPC.
     *
     * @param request   the query request to send to the node
     * @param timeout   the timeout for the request in milliseconds, or null for none
     * @param callback  the callback to execute after the request has completed
     */
    public <Q extends RpcRequest<R>, R extends RpcResponse> Future<R> processRequestAsync(Q request, Integer timeout, QueryCallback<R> callback) {
        if(request == null) throw new IllegalArgumentException("Request argument must not be null");
        if(timeout != null && timeout < 0) throw new IllegalArgumentException("Timeout period must be positive or null");
        
        return RpcQueryNode.executorService.submit(() -> {
            try {
                R response = RpcQueryNode.this.processRequest(request, timeout);
                if(callback != null) callback.onResponse(response);
                return response;
            } catch (Exception e) {
                if(callback != null) callback.onFailure(e);
                throw e;
            }
        });
    }
    
    
    /**
     * Sends a raw JSON query to the RPC server, and then returns the raw JSON response.
     *
     * @param jsonRequest   the JSON query to send to the node
     * @param con           a HTTP connection with the node to send the query to
     * @return the JSON response from the node
     * @throws IOException if an error occurs with the connection to the node
     */
    protected String processRawRequest(String jsonRequest, HttpURLConnection con) throws IOException {
        if(jsonRequest == null) throw new IllegalArgumentException("JSON request string cannot be null");
        
        //Configure connection
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestMethod("POST");
        if(this.authToken != null) {
            con.setRequestProperty("Authorization", "Bearer " + this.authToken); //Set authorization token header
        }
        
        //Write request data
        OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
        writer.write(jsonRequest);
        writer.close();
    
        //Read response data
        InputStreamReader input = new InputStreamReader(con.getInputStream());
        BufferedReader inputReader = new BufferedReader(input);
        
        StringBuilder response = new StringBuilder();
        String line;
        while((line = inputReader.readLine()) != null) response.append(line);
        inputReader.close();
    
        return response.toString();
    }
    
    
    /**
     * Converts a pure JSON string into a response instance.
     *
     * @param responseJson  the JSON to deserialize
     * @param responseClass the response class to deserialize into
     * @return the deserialized response instance
     */
    protected <R extends RpcResponse> R deserializeResponseFromJSON(String responseJson, Class<R> responseClass) throws RpcException {
        JsonObject response;
        try {
            response = RpcQueryNode.JSON_PARSER.parse(responseJson).getAsJsonObject(); //Parse response
        } catch (JsonSyntaxException ex) {
            throw new RpcInvalidResponseException(responseJson, ex); //If unable to parse
        }
        
        //Check for returned RPC error
        JsonElement responseError = response.get("error");
        if(responseError != null) throw this.parseException(responseError.getAsString());
        
        //Deserialize response
        R responseObj = this.gson.fromJson(responseJson, responseClass); //Deserialize from JSON
        responseObj.init(response); //Initialise raw parameters
        return responseObj;
    }
    
    
    /**
     * Parses a returned error string into the appropriate RpcException type.
     *
     * @param message  the returned error message from the node
     * @return the matching exception to be thrown
     */
    protected RpcException parseException(String message) {
        String msgLower = message.toLowerCase();
        
        switch(msgLower) {
            case "wallet is locked":
            case "wallet locked":                   return new RpcWalletLockedException();              //Wallet locked
            case "insufficient balance":            return new RpcInvalidArgumentException(message);    //Invalid/bad argument (not enough funds)
            case "invalid authorization header":    return new RpcInvalidAuthTokenException();          //Invalid auth token
            case "rpc control is disabled":         return new RpcControlDisabledException();           //RPC disabled
            case "unable to parse json":            return new RpcInvalidRequestJsonException();        //Invalid request body
            case "unknown command":                 return new RpcUnknownCommandException();            //Unknown command
        }
        
        if(msgLower.startsWith("bad")
                || msgLower.startsWith("invalid")
                || msgLower.endsWith("required"))   return new RpcInvalidArgumentException(message);    //Invalid/bad argument
        
        if(msgLower.contains("not found"))          return new RpcEntityNotFoundException(message);     //Unknown referenced entity
        
        if(msgLower.startsWith("internal"))         return new RpcInternalException(message);           //Internal server error
        
        return new RpcException(message); //Default to regular
    }
    
    
    /**
     * Converts a request instance into a pure JSON string.
     *
     * @param req the request to serialize
     * @return the serialized JSON command
     */
    public String serializeRequestToJSON(RpcRequest<?> req) {
        if(req == null) throw new IllegalArgumentException("Query request argument cannot be null");
        return this.gson.toJson(req);
    }

}
