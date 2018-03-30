package in.bigdolph.jnano.rpc.query;

import com.google.gson.Gson;
import in.bigdolph.jnano.rpc.exception.RPCQueryException;
import in.bigdolph.jnano.rpc.query.RPCQueryNode;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;
import in.bigdolph.jnano.tests.Configuration;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public class TestNode extends RPCQueryNode {
    
    public TestNode() throws MalformedURLException {
        super(Configuration.NODE_IP, Configuration.NODE_PORT);
    }
    
    @Override
    public String processRawRequest(String jsonRequest, HttpURLConnection con) throws IOException {
        System.out.println("Processing query: " + jsonRequest);
        String response = super.processRawRequest(jsonRequest, con);
        System.out.println("Response: " + response);
        return response;
    }
    
    @Override
    public <Q extends RPCRequest<R>, R extends RPCResponse> R processRequest(Q request) throws IOException, RPCQueryException {
        return super.processRequest(request);
    }
    
}
