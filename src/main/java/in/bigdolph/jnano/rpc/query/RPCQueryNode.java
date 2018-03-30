package in.bigdolph.jnano.rpc.query;

import com.google.gson.*;
import in.bigdolph.jnano.model.block.Block;
import in.bigdolph.jnano.rpc.adapters.BlockTypeDeserializer;
import in.bigdolph.jnano.rpc.adapters.BooleanTypeDeserializer;
import in.bigdolph.jnano.rpc.adapters.hotfix.ArrayTypeAdapterFactory;
import in.bigdolph.jnano.rpc.adapters.hotfix.CollectionTypeAdapterFactory;
import in.bigdolph.jnano.rpc.adapters.hotfix.MapTypeAdapterFactory;
import in.bigdolph.jnano.rpc.exception.RPCQueryException;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RPCQueryNode {
    
    protected static final JsonParser JSON_PARSER = new JsonParser();
    
    protected static final ExecutorService executorService = Executors.newCachedThreadPool();
    
    protected final URL address;
    protected final Gson gson;
    
    
    /**
     * Constructs a new query node with the address 127.0.0.1:7076
     *
     * @throws MalformedURLException if the address cannot be parsed
     */
    public RPCQueryNode() throws MalformedURLException {
        this("127.0.0.1", 7076); //Local address and default port
    }
    
    /**
     * Constructs a new query node with the given address and port
     *
     * @param address   the address of the node
     * @param port      the port which the node is listening on
     * @throws MalformedURLException if the address cannot be parsed
     */
    public RPCQueryNode(String address, int port) throws MalformedURLException {
        this(new URL("HTTP", address, port, ""));
    }
    
    /**
     * Constructs a new query node with the given address and port
     * @param address   the HTTP URL (address and port) which the node is listening on
     */
    public RPCQueryNode(URL address) {
        this(address, new GsonBuilder());
    }
    
    protected RPCQueryNode(URL address, GsonBuilder gson) {
        this.address = address;
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
     * Sends a query request to the node via RPC.
     * This method will not timeout as long as the connection remains open.
     *
     * @param request the query request to send to the node
     * @return the successful reponse from the node
     * @throws IOException         if an error occurs with the connection to the node
     * @throws RPCQueryException   if the node returns a non-successful response
     */
    public <Q extends RPCRequest<R>, R extends RPCResponse> R processRequest(Q request) throws IOException, RPCQueryException {
        return this.processRequest(request, null);
    }
    
    /**
     * Sends a query request to the node via RPC.
     *
     * @param request the query request to send to the node
     * @param timeout the timeout for the request in milliseconds, or null for none
     * @return the successful reponse from the node
     * @throws IOException         if an error occurs with the connection to the node
     * @throws RPCQueryException   if the node returns a non-successful response
     */
    public <Q extends RPCRequest<R>, R extends RPCResponse> R processRequest(Q request, Integer timeout) throws IOException, RPCQueryException {
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
    public <Q extends RPCRequest<R>, R extends RPCResponse> Future<R> processRequestAsync(Q request, QueryCallback<R> callback) {
        return this.processRequestAsync(request, null, callback);
    }
    
    /**
     * Sends a query request to the node via RPC.
     *
     * @param request   the query request to send to the node
     * @param timeout   the timeout for the request in milliseconds, or null for none
     * @param callback  the callback to execute after the request has completed
     */
    public <Q extends RPCRequest<R>, R extends RPCResponse> Future<R> processRequestAsync(Q request, Integer timeout, QueryCallback<R> callback) {
        if(request == null) throw new IllegalArgumentException("Request argument must not be null");
        if(timeout != null && timeout < 0) throw new IllegalArgumentException("Timeout period must be positive or null");
        
        return RPCQueryNode.executorService.submit(() -> {
            try {
                R response = RPCQueryNode.this.processRequest(request, timeout);
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
    protected <R extends RPCResponse> R deserializeResponseFromJSON(String responseJson, Class<R> responseClass) throws RPCQueryException {
        JsonObject response = RPCQueryNode.JSON_PARSER.parse(responseJson).getAsJsonObject(); //Parse response
        
        //Check for returned RPC error
        JsonElement responseError = response.get("error");
        if(responseError != null) throw new RPCQueryException(responseError.getAsString());
        
        R responseObj = this.gson.fromJson(responseJson, responseClass); //Deserialize from JSON
        responseObj.init(response); //Initialise raw parameters
        
        return responseObj;
    }
    
    
    /**
     * Converts a request instance into a pure JSON string.
     *
     * @param req the request to serialize
     * @return the serialized JSON command
     */
    public String serializeRequestToJSON(RPCRequest<?> req) {
        if(req == null) throw new IllegalArgumentException("Query request argument cannot be null");
        return this.gson.toJson(req);
    }

}
