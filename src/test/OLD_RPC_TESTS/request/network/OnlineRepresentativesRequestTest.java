package uk.oczadly.karl.jnano.rpc.request.network;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import uk.oczadly.karl.jnano.rpc.QueryBaseTest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccounts;
import uk.oczadly.karl.jnano.tests.NodeTests;


public class OnlineRepresentativesRequestTest extends QueryBaseTest {
    
    @Test @Category(NodeTests.class)
    public void test() {
        ResponseAccounts res = query(new OnlineRepresentativesRequest());
        Assert.assertNotNull(res.getAccounts());
    }
    
}