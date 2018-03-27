package in.bigdolph.jnano.rpc.query.request.wallet;

import in.bigdolph.jnano.rpc.query.QueryBaseTest;
import in.bigdolph.jnano.rpc.query.response.generic.AccountsResponse;
import in.bigdolph.jnano.tests.NodeTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Suite;

import static org.junit.Assert.*;

public class AccountsCreateRequestTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void test() {
        AccountsResponse res = query(new AccountsCreateRequest(TEST_WALLET, 3, false));
        
        assertNotNull(res.getAccounts());
        assertEquals(3, res.getAccounts().size());
    }
    
}