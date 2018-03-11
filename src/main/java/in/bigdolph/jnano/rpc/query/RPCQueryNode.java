package in.bigdolph.jnano.rpc.query;

import com.google.gson.*;
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

public class RPCQueryNode {
    
    protected static final JsonParser JSON_PARSER = new JsonParser();
    
    protected final URL address;
    protected final Gson gson;
    
    
    public RPCQueryNode() throws MalformedURLException {
        this("127.0.0.1", 7076); //Local address and default port
    }
    
    public RPCQueryNode(String address, int port) throws MalformedURLException {
        this(new URL("HTTP", address, port, ""));
    }

    public RPCQueryNode(URL address) {
        this(address, new GsonBuilder());
    }
    
    protected RPCQueryNode(URL address, GsonBuilder gson) {
        this.address = address;
        this.gson = gson.excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapterFactory(new ArrayTypeAdapterFactory())      //Empty array hotfix
                .registerTypeAdapterFactory(new CollectionTypeAdapterFactory()) //Empty collection hotfix
                .registerTypeAdapterFactory(new MapTypeAdapterFactory())        //Empty map hotfix
                .create();
    }
    
    
    
    public final URL getAddress() {
        return this.address;
    }
    
    
    /** Sends a query request to the node via RPC.
     *
     * @param request The query request to compute
     * @return The reponse from the node
     * @throws RPCQueryException
     */
    public <Q extends RPCRequest<R>, R extends RPCResponse> R processRequest(Q request) throws IOException, RPCQueryException {
        //Serialise the request into JSON
        String requestJsonStr = this.serializeRequestToJSON(request);
        
        //Send the request to the node
        String responseJsonStr = this.processRawRequest(requestJsonStr);
        
        //Parse response
        JsonObject responseJson = RPCQueryNode.JSON_PARSER.parse(responseJsonStr).getAsJsonObject();
        
        //Check for returned RPC error
        JsonElement responseError = responseJson.get("error");
        if(responseError != null) throw new RPCQueryException(responseError.getAsString());
        
        //Deserialize from JSON
        R response = this.gson.fromJson(responseJson, request.getResponseClass());
        
        //Initialise raw parameters
        response.init(responseJson);
        
        return response;
    }
    
    
//    /** Sends a query request to the node via RPC.
//     *
//     * @param request The query request to compute
//     * @param callback The response
//     * @throws RPCQueryException
//     */
//    public <Q extends RPCRequest<R>, R extends RPCResponse> void processRequestAsync(Q request, QueryCallback<Q, R> callback) throws IOException, RPCQueryException {
//        return; //TODO
//    }
    
    
    protected String processRawRequest(String jsonRequest) throws IOException {
        if(jsonRequest == null) throw new IllegalArgumentException("JSON request string cannot be null");
        
        //Open connection
        HttpURLConnection con = (HttpURLConnection)this.address.openConnection();
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
    
    
    public String serializeRequestToJSON(RPCRequest<?> req) {
        if(req == null) throw new IllegalArgumentException("Query request argument cannot be null");
        return this.gson.toJson(req);
    }

}
