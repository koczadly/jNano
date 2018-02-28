package in.bigdolph.jnano.rpc.query;

import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;
import junit.framework.TestCase;
import org.junit.Before;

import java.io.IOException;
import java.net.MalformedURLException;


public abstract class QueryBaseTest {
    
    private RPCQueryNode node;
    
    
    @Before
    public void setUp() throws MalformedURLException {
        this.node = new RPCQueryNode();
    }
    
    
    
    protected <R extends RPCResponse> R query(RPCRequest<R> req) {
        try {
            return this.node.processRequest(req);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}