package uk.oczadly.karl.jnano.rpc.request.wallet;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import uk.oczadly.karl.jnano.rpc.QueryBaseTest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccounts;
import uk.oczadly.karl.jnano.tests.Configuration;
import uk.oczadly.karl.jnano.tests.NodeTests;

import static org.junit.Assert.*;

public class AccountsCreateRequestTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void test() {
        ResponseAccounts res = query(new RequestAccountsCreate(Configuration.TEST_WALLET, 3, false));
        
        assertNotNull(res.getAccounts());
        assertEquals(3, res.getAccounts().size());
    }
    
}