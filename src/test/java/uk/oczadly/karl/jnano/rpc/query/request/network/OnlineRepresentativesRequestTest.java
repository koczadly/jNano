package uk.oczadly.karl.jnano.rpc.query.request.network;

import uk.oczadly.karl.jnano.rpc.query.QueryBaseTest;
import uk.oczadly.karl.jnano.rpc.query.response.AccountsResponse;
import uk.oczadly.karl.jnano.tests.NodeTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


public class OnlineRepresentativesRequestTest extends QueryBaseTest {
    
    @Test @Category(NodeTests.class)
    public void test() {
        AccountsResponse res = query(new OnlineRepresentativesRequest());
        Assert.assertNotNull(res.getAccounts());
    }
    
}