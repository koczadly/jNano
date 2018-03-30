package in.bigdolph.jnano.rpc.query;

import in.bigdolph.jnano.rpc.query.exception.RpcQueryException;
import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.RpcResponse;
import in.bigdolph.jnano.tests.Configuration;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public class TestNode extends RpcQueryNode {
    
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
    public <Q extends RpcRequest<R>, R extends RpcResponse> R processRequest(Q request) throws IOException, RpcQueryException {
        return super.processRequest(request);
    }
    
}
