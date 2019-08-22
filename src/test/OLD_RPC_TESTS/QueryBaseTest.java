package uk.oczadly.karl.jnano.rpc;

import org.junit.Assert;
import org.junit.Before;
import uk.oczadly.karl.jnano.rpc.exception.RpcException;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;
import uk.oczadly.karl.jnano.tests.Configuration;

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