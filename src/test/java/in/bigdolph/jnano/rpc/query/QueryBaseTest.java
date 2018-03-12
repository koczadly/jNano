package in.bigdolph.jnano.rpc.query;

import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.request.TestNode;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;
import junit.framework.TestCase;
import org.junit.Before;

import java.io.IOException;
import java.net.MalformedURLException;


public abstract class QueryBaseTest {
    
    public static final String TEST_ACCOUNT = "xrb_365md53cciaquuerrp97psfrsg1bd4rw5d8cyfpyefgofsopiuzozibdnbmx";
    
    private RPCQueryNode node;
    
    
    @Before
    public void setUp() throws MalformedURLException {
        this.node = new TestNode();
    }
    
    
    
    public RPCQueryNode getNode() {
        return node;
    }
    
    
    public <R extends RPCResponse> R query(RPCRequest<R> req) {
        try {
            return this.node.processRequest(req);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}