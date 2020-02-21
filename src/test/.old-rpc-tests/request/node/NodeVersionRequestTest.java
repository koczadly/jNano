package uk.oczadly.karl.jnano.rpc.request.node;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import uk.oczadly.karl.jnano.rpc.QueryBaseTest;
import uk.oczadly.karl.jnano.rpc.response.ResponseVersion;
import uk.oczadly.karl.jnano.tests.NodeTests;

import static org.junit.Assert.*;

public class NodeVersionRequestTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void test() {
        ResponseVersion res = query(new RequestVersion());
        
        assertNotNull(res.getNodeVendor());
        assertNotEquals(0, res.getRPCVersion());
        assertNotEquals(0, res.getStoreVersion());
    
        System.out.println(res.getNodeVendor());
        System.out.println(res.getRPCVersion());
        System.out.println(res.getStoreVersion());
    }
    
}