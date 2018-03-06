package in.bigdolph.jnano.rpc.query.request;

import com.google.gson.Gson;
import in.bigdolph.jnano.rpc.exception.RPCQueryException;
import in.bigdolph.jnano.rpc.query.RPCQueryNode;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.io.IOException;
import java.net.MalformedURLException;

public class TestNode extends RPCQueryNode {
    
    public TestNode() throws MalformedURLException {
        super();
    }
    
    
    @Override
    public String processRawRequest(String jsonRequest) throws IOException {
        System.out.println("Processing query: " + jsonRequest);
        String response = super.processRawRequest(jsonRequest);
        System.out.println("Response: " + response);
        return response;
    }
    
    @Override
    public <Q extends RPCRequest<R>, R extends RPCResponse> R processRequest(Q request) throws IOException, RPCQueryException {
        return super.processRequest(request);
    }
    
    public Gson getGson() {
        return this.gson;
    }
    
}
