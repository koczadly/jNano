package uk.oczadly.karl.jnano.rpc.query.request.node;

import uk.oczadly.karl.jnano.rpc.query.QueryBaseTest;
import uk.oczadly.karl.jnano.rpc.query.response.NodeVersionResponse;
import uk.oczadly.karl.jnano.tests.NodeTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.*;

public class NodeVersionRequestTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
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