package in.bigdolph.jnano.rpc.query.request.node;

import in.bigdolph.jnano.rpc.query.QueryBaseTest;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeVersionRequestTest extends QueryBaseTest {
    
    @Test
    public void test() {
        NodeVersionResponse res = query(new NodeVersionRequest());
        
        assertNotNull(res.getNodeVendor());
        assertNotEquals(0, res.getRPCVersion());
        assertNotEquals(0, res.getStoreVersion());
    
        System.out.println(res.getNodeVendor());
        System.out.println(res.getRPCVersion());
        System.out.println(res.getStoreVersion());
    }
    
}