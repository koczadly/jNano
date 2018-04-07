package in.bigdolph.jnano.rpc.query;

import in.bigdolph.jnano.rpc.query.exception.RpcException;
import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.RpcResponse;
import in.bigdolph.jnano.tests.Configuration;
import org.junit.Assert;
import org.junit.Before;

import java.io.IOException;
import java.net.MalformedURLException;


public abstract class QueryBaseTest implements Configuration {
    
    private TestNode node;
    
    
    @Before
    public void setUp() throws MalformedURLException {
        this.node = new TestNode();
    }
    
    
    
    public TestNode getNode() {
        return node;
    }
    
    
    public <R extends RpcResponse> R query(RpcRequest<R> req) {
        try {
            R response = this.node.processRequest(req);
            Assert.assertNotNull(response);
            return response;
        } catch (IOException | RpcException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public <R extends RpcResponse> R query(Class<R> responseClass, String responseJson) {
        try {
            return this.node.deserializeResponseFromJSON(responseJson, responseClass);
        } catch(RpcException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}