package uk.oczadly.karl.jnano.rpc.query.request.ledger;

import uk.oczadly.karl.jnano.rpc.query.QueryBaseTest;
import uk.oczadly.karl.jnano.rpc.query.response.AccountsPendingResponse;
import uk.oczadly.karl.jnano.tests.NodeTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import uk.oczadly.karl.jnano.tests.Configuration;

import static org.junit.Assert.*;

public class AccountsPendingRequestTest extends QueryBaseTest {
    
    @Test
    @Category(NodeTests.class)
    public void test() {
        AccountsPendingResponse res = query(new AccountsPendingRequest(new String[] {Configuration.TEST_ACCOUNT}, 100));
        assertNotNull(res.getPendingBlocks());
        assertNotNull(res.getPendingBlocks(Configuration.TEST_ACCOUNT));
        assertEquals(1, res.getPendingBlocks().keySet().size());
        assertEquals(1, res.getPendingBlockHashes(Configuration.TEST_ACCOUNT).size());
    
        res = query(new AccountsPendingRequest(new String[] {}, 1));
        assertNotNull(res.getPendingBlocks());
        assertEquals(0, res.getPendingBlocks().keySet().size());
    }
    
}