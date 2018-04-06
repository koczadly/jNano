package in.bigdolph.jnano.rpc.query.request.network;

import in.bigdolph.jnano.rpc.query.QueryBaseTest;
import in.bigdolph.jnano.rpc.query.response.AccountsResponse;
import in.bigdolph.jnano.tests.NodeTests;
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